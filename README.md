### はじめに

2017年に入って[Dagger2](https://google.github.io/dagger/)もバージョン2.9を迎えました.  
Androidでも使われることが多いDIフレームワークも, バージョンを重ねるごとに便利なAPIが増えています.  

本稿はAndroidアプリを例に, `Subcomponent`と`Activity`に依存する`Component`へのインジェクションについて, 過去のAPIを使用した方法と, 新しいAPIを使用する方法とで比較を行い, どのようなAPIが使えるようになっているのかを例示したいと思います.  

今回紹介する内容＋αと動くソースコードはGitHubにもアップしています.  
そちらもあわせてご覧ください :)  

⭐️押したくなるGitHubページへのリンク


### Subcomponent. 親と子の密結合問題

Androidでは, コンポーネントをアクティビティの単位で分割することがよくあります（e.g. `MainActivityComponent`, `SettingActivityComponent`, etc.）  
そのようなコンポーネントがアプリケーションスコープのオブジェクトを参照する場合や, 他のコンポーネントからも参照される共有オブジェクトを参照する場合は`Subcomponent`として定義される場合があります.  
サブコンポーネントの仕組みは, それぞれのアクティビティコンポーネントの共通部分をまとめて定義したり, スコープの観点からみた"親-子"を定義したりするのに便利です.  

ただ, 古いバージョンのDaggerではサブコンポーネントに依存される親コンポーネントは, サブコンポーネントのクラスを知っている必要があり親と子の結合度が高くなる問題がありました.  

```java
@Component(...)
public interface AppComponent {
  // 親であるAppComponentは子にあたるコンポーネントを全て知っておく必要がある :(
  MainActivityComponent plus(MainActivityModule module);
  SettingActivityComponent plus(SettingActivityModule module);
```

この問題はDagger2.7で追加された[`Module`の`subcomponent`属性](https://google.github.io/dagger/api/latest/dagger/Component.Builder.html)を使うことで解決できます.  


#### Module.subcomponents
 
 `@Module`の`subcomponent`属性はサブコンポーネントの親を指定するための新しい手段を提供します.  
 `subcomponent`には対象のサブコンポーネントクラスを定義します.  

```java
@Module(subcomponents = {MainComponent.class, SubComponent.class})
public abstract class ActivityBindingModule {
}
```

このモジュールは親コンポーネントの`modules`に定義されることで, 親コンポーネントに属することになり,
 結果的に親コンポーネントと子コンポーネントの関係を築くことになります.  
これは, 親コンポーネントとの関係を築く, 過去の`Component.plus`処理相当に代わる方法です.  
重要なポイントは親コンポーネントがサブコンポーネントの詳細（クラス名）を知らなくて済むというところです.  

```java
@Singleton
@Component(modules = {AppModule.class, ActivityBindingModule.class})
public interface AppComponent {
  // 従来の plus(MainComponent subcomponent); な処理はもういらない;)
}
```

この方法を使う場合, `@Module(subcomponents={...})`で指定したサブコンポーネントをどのように構築するのかをDaggerに教える必要があります.  
サブコンポーネントの内部インタフェースとしてコンポーネントのビルダを新たに定義し, これに`Component.Builder`のアノテーション をつければ準備完了です.  
（サブ）コンポーネントビルダーには[いくつかの実装ルールがある](https://google.github.io/dagger/api/latest/dagger/Subcomponent.Builder.html)のでそれに従います.  

```java
@Subcomponent(modules = MainModule.class)
public interface MainComponent {
  @Subcomponent.Builder interface Builder {
    Builder activityModule(MainModule module);
    MainComponent build();
  }
```

サブコンポーネントのビルダーはDaggerによってプライベートクラスとして自動生成されます.  必要なビルダーは`@Inject`などで取得します.  

```java
public class App extends Application {
  @Inject MainComponent.Builder mainComponentBuilder;
  @Inject SettingComponent.Builder settingComponentBuilder;

  ...

  public MainComponent.Builder mainComponentBuilder() {
    return mainComponentBuilder;
  }

  public SettingComponent.Builder settingComponentBuilder() {
    return settingComponentBuilder;
  }
```

ここまでで, サブコンポーネント（厳密には親コンポーネント）が抱えていた親子間の密結合問題を解消できました.  
しかし, 今度はコンポーネントビルダーを取得するクラス（上の例で言えばアプリケーションクラス）が各サブコンポーネントと密結合してしまいました.  
このままでは, サブコンポーネントが増えるとアプリケーションクラスにまで手が入ってしまいます.  
この次は, マルチバインディングの機能を使ってこの問題に対処します. 


### Multibinding. コンポーネントマップの自動生成

Dagger2.4から, 生成したオブジェクトをコレクションにバインドするマルチバインディング機能が追加されました.  
これにより, プリセット状態の`Set`や`Map`をインジェクションできるようになりました.  
今回はこのマルチバインディングを使ってサブコンポーネントビルダーの`Map`コレクションを作り, サブコンポーネントを柔軟に追加できるように改良していきます.  

まずはじめに, マルチバインディングマップへ格納するための`Key`と`Value`を決めておきます.  
Androidではコンポーネットを`Activity`の単位で分割することが多いので`Key`には`Activity`のクラスオブジェクトを格納し, `Value`にはコンポーネントビルダーを格納することにします.  

アプリケーションクラスが各`Activity`の詳細を知らなくてもいいように, `Activity`に直接関わるコンポーネントとモジュールを抽象化した`ActivityComponent`インタフェースと`ActivityModule`インタフェースを作成します. いまはこれらをマーカーインタフェースとして宣言しておきます.  

```java
public interface ActivityComponent {
}

public interface ActivityModule {
}

// Activityと関連するコンポーネントとモジュールはこれを実装する
@Subcomponent(modules = MainComponent.MainModule.class)
public interface MainComponent extends ActivityComponent<MainActivity> {
  ...
  @Module
  class MainModule extends ActivityModule {
    ...
  }
}
```

コンポーネントビルダに関しても抽象化します.  
上記で定義したインタフェースを使って, `ActivityModule`を受け取り`ActivityComponent`をビルドして返すビルダーインタフェースを定義します.  

```java
public interface ActivityComponentBuilder<M extends ActivityModule, C extends ActivityComponent> {
  ActivityComponentBuilder<M, C> activityModule(M activityModule);

  C build();
}

// Activityと関連するサブコンポーネントビルダはこれを実装する
@Subcomponent.Builder
interface Builder extends ActivityComponentBuilder<MainModule, MainComponent> {
}
```

アプリがアクティビティコンポーネントを取得したい場合の手順は次の通りです.  

 1. `ActivityComponentBuilder`を実装したコンポーネントビルダを取得
 2. 必要な`ActivityModule`をビルダに設定する
 3. `build`メソッドで`ActivityComponent`をビルドして取得

今時点ではまだコンポーネントビルダを取得するために具体的なクラス名を指定する必要があります.  

```java
public class App extends Application {

  @Inject MainComponent.Builder mainComponentBuilder;
  @Inject SettingComponent.Builder settingComponentBuilder;
```

次は, 用意した`ActivityComponent`, `ActivityModule`, `ActivityComponentBuilder`とマルチバインディング機能を使って, いよいよアプリケーションクラスから`Activity`との依存関係を取り除いていきます :)



マルチバインディングマップに格納するオブジェクトを提供する際にはキーとする値を指定します.  
今回は, Activityのクラス情報をキーとするので専用のアノテーションを作成します.  
キーとして`Class`を受け取り, その型を`Activity`のサブクラスに制約するジェネリクスを指定しておくのがポイントです.  
これで, マルチバインディングマップのキーには`Activity`クラスだけが許可されるようになります.  

```java
@MapKey
public @interface ActivityMapKey {
  Class<? extends Activity> value();
}
```

それでは, マルチバインディングマップを定義していきましょう.  
マルチバインディングマップを提供する方法は他のオブジェクトと同じくモジュールのメソッドとして定義します.  
今回はマルチバインディングを使ってサブコンポーネントビルダーの`Map`コレクションを作り, サブコンポーネントを柔軟に追加できるように改良していくのが目的なので, これを定義するモジュールは`@Module(subcomponents=...)`を定義した`ActivityBindingModule`が妥当でしょう.  

`ActivityBindingModule`にマルチバインディングマップの定義を追加したものが下記です.  

```java
@Module(subcomponents = {MainComponent.class, SubComponent.class})
public abstract class ActivityBindingModule {
  @Provides @IntoMap @ActivityMapKey(MainActivity.class)
  public ActivityComponentBuilder mainComponentBuilder(
      MainComponent.Builder builder) {
    return builder;
  }

  @Provides @IntoMap @ActivityMapKey(SettingActivity.class)
  public ActivityComponentBuilder settingComponentBuilder(
      SettingComponent.Builder builder) {
    return builder;
  }
}
```

さらに, Dagger2.4から導入された`@Binds`を使えば, こういったボイラープレートなプロバイダーメソッドの定義を簡略化できます.  
Dagger2.5からはマルチバインディングに対しても使えるようになったので, これを使ってシンプルに定義したものが下記です.  

```java
@Module(subcomponents = {MainComponent.class, SettingComponent.class})
public abstract class ActivityBindingModule {
  @Binds @IntoMap @ActivityMapKey(MainActivity.class)
  public abstract ActivityComponentBuilder mainComponentBuilder(
      MainComponent.Builder builder);

  @Binds @IntoMap @ActivityMapKey(SettingActivity.class)
  public abstract ActivityComponentBuilder settingComponentBuilder(
      SettingComponent.Builder builder);
}
```

さて, これで`Class<? extends Activity>`な型をキーに持ち, 値には`ActivityComponentBuilder`な型が格納される`Map`を自動生成できるようになりました.  
これを提供するプロバイダーメソッドは親コンポーネントにあたる`AppComponent`に定義しておきましょう.  

```java
@Component(modules = { AppComponent.AppModule.class, ActivityBindingModule.class })
public interface AppComponent {
  Map<Class<? extends Activity>, ActivityComponentBuilder> activityComponentBuilders();
```

こうすることで, Daggerはマルチバインディングによってアクティビティコンポーネントのマップを構築し, それを`activityComponentBuilders()`経由で取得できるようになります.  

これでついにアプリケーションクラスから`Activity`の詳細な情報を排除することができました. Yay!  

アクティビティとサブコンポーネントが追加になってもアプリケーションクラスを変更する理由はもはやありません.  
アクティビティに関係するサブコンポーネントに追加・変更がある場合の修正は`ActivityBindingModule`に閉じています.  


#### _plus_ one

これで一通りの実装は完了ですが, もう一歩進めましょう.  

アクティビティのコンポーネントには`inject`メソッドを生やすことがよくあります.  
なので`ActivityComponent`にこれを定義します.  

```java
public interface ActivityComponent<T extends Activity> extends MembersInjector<T> {
}
```

もうひとつ, `ActivityModule`にはアクティビティインスタンスを保持させることがよくあるので, その定義をしておきます.  

```java
@Module
public abstract class ActivityModule<T extends Activity> {
  protected final T activity;

  public ActivityModule(T activity) {
    this.activity = activity;
  }

  @Provides public T provideActivity() {
    return activity;
  }
}
```

アクティビティコンポーネントを取得するときは, 下記の手順です.  

 1. `ActivityComponentBuilder`を実装したコンポーネントビルダを取得
 2. 必要な`ActivityModule`をビルダに設定する
 3. `build`メソッドで`ActivityComponent`をビルドして取得

`ActivityComponentBuilder`を取得するメソッドは以前と同様, 親コンポーネントにあたる`AppComponent`に定義されているので, 下記の要領で取得します.  

```java
Map<Class<? extends Activity>, ActivityComponentBuilder> map = 
    ((AppComponent) context.getApplicationContext().activityComponentBuilders();
map.get(activity.getClass());
```

いい感じですね. ここまでで一旦主要なクラス達を整理しておきます.  

```java
// アクティビティのコンポーネントを表現するインタフェース
public interface ActivityComponent<T extends Activity> extends MembersInjector<T> {...}

// アクティビティのモジュールを表現する基底クラス
public abstract class ActivityModule<T extends Activity> {...}

// 具体的なアクティビティコンポーネントとそのビルダとモジュール
@Subcomponent(modules = MainComponent.MainModule.class)
public interface MainComponent extends ActivityComponent<MainActivity> {
  @Subcomponent.Builder
  interface Builder extends ActivityComponentBuilder<MainModule, MainComponent> {
  }
 
  @Module
  class MainModule extends ActivityModule<MainActivity> {
    ...
  }
}

// アクティビティモジュールのバインドを定義するクラス
@Module(subcomponents = { MainComponent.class })
public abstract class ActivityBindingModule {
  @Singleton @Binds @IntoMap @ActivityMapKey(MainActivity.class)
  public abstract ActivityComponentBuilder mainActivityComponentBuilder(
      MainComponent.Builder builder);
}

// 親コンポーネントにあたるアプリケーションコンポーネント
@Component(modules = { AppModule.class, ActivityBindingModule.class })
public interface AppComponent {
  Map<Class<? extends Activity>, ActivityComponentBuilder> activityComponentBuilders();
  @Module
  class AppModule { ... }
}

// アクティビティコンポーネントを取得するコード
Map<Class<? extends Activity>, ActivityComponentBuilder> map = 
    ((AppComponent) context.getApplicationContext().activityComponentBuilders();
map.get(activity.getClass());
```

Daggerの新しいAPIを使ってアクティビティコンポーネントをスマートに取得することができました :)

さらに, さらに, もう一歩進めた内容を次稿に載せます.  
Daggerによってスコープ制御されているコンポーネントをMortarライブラリで更に使いやすくする方法です.  

今回紹介した内容＋αと動くソースコードをGitHubにもアップしています.  
そちらもあわせてご覧ください :)  

⭐️押したくなるGitHubページへのリンク

以上です.  

