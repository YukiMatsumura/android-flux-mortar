### はじめに

2017年に入って[Dagger2](https://google.github.io/dagger/)もバージョン2.9を迎えました（そんな[v.2.9.0には問題がある](https://github.com/google/dagger/issues/577)のでご注意を）  
Androidでも使われることが多いDIフレームワークですが, バージョンを重ねるごとに便利なAPIが増えています.  

本稿はAndroidアプリを例に, `Subcomponent`と`Activity`に依存する`Component`へのインジェクション周りについて, 過去のAPIを使用した方法と, 最近のAPIを使用する方法とで比較を行い, どのようなAPIが使えるようになっているのかを例示したいと思います.  


### Subcomponent. 親と子の密結合関係

Androidでは, Daggerの`Component`を`Activity`の単位で分割することが多いと思います（e.g. `MainActivityComponent`, `SettingActivityComponent`, etc.）  
アプリケーションスコープのオブジェクトを扱う場合や, 他の`ActivityComponent`からも共通的に参照されるオブジェクトがある場合は`Subcomponent`としてこれを定義することがあります.  
`Subcomponent`の仕組みは各々の`ActivityComponent`が持つ共通部分をまとめて定義したり, スコープの観点からみた"親-子"を定義したりするのに便利です.  

ただ, 古いバージョンのDaggerでは`Subcomponent`に依存される親コンポーネントは, 子コンポーネントのクラスを知っている必要があり親と子の結合度が高くなる問題があります.  

```java
@Component(...)
public interface AppComponent {
  // 親であるAppComponentは子になるコンポーネントを全て知っておかないといけない:(
  MainActivityComponent plus(MainActivityModule module);
  SettingActivityComponent plus(SettingActivityModule module);
```

この問題はDagger2.7で追加された[`Module`の`subcomponent`属性](https://google.github.io/dagger/api/latest/dagger/Component.Builder.html)を使うことで解決できます.  

#### Module.subcomponents
 
 `Module`の`subcomponent`属性はサブコンポーネントの親を指定するための新しい手段を提供してくれます.  
 `subcomponent`にはサブコンポーネントクラスのリストを定義し, `@Module`アノテーションの属性として指定します.  

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


### Multibinding. コンポーネットマップの自動生成

Dagger2.xから, 生成したオブジェクトをコレクションにバインドするマルチバインディング機能が追加されました.  
これにより, あらかじめ値が格納された状態の`Set`や`Map`をインジェクションすることが可能になります.  
今回はこのマルチバインディングでサブコンポーネントビルダの`Map`を作り, サブコンポーネントを柔軟に追加できるように改良していきます.  

まず, マルチバインディングマップへ格納するための`Key`と`Value`を決めます.  
Androidでは（サブ）コンポーネットを`Activity`の単位で分割することが多いので`Key`には`Activity`のクラスオブジェクトを, `Value`にはサブコンポーネントビルダーを格納することにします.  

この後で楽をするために, `Activity`に直接関わるコンポーネントとモジュールをそれぞれ`ActivityComponent`, `ActivityModule`に抽象化します.  
これは, マルチバインディングマップに格納できる値の型を制限するためにも使います.  

```java
public interface ActivityComponent<T extends Activity> {
}

public abstract class ActivityModule {
}
```

この時点のマルチバインディングマップの定義を見てみましょう.  
マルチバインディングマップであることを明示するには`@IntoMap`を使います. 
また, サブコンポーネントの生成なので

```java
ここにマルチバインディングマップの定義を追加
```

マルチバインディングマップのキーに指定できるクラス情報も`Activity`に縛りましょう.  
`ActivitMapKey`アノテーションを定義していきます.  
（キーとなるオブジェクトの型が異なると別々のマルチバインディングマップになってしまいます）

```java
ここにActivityMapKeyの定義
```

作成した`ActivityMapKey`を使うように先程のバインディングマップの定義を修正します.  

```java
ActivityMapKeyを使ったマップ定義をここに
```

さらに, Dagger2.xからはこういった単純なプロバイダーメソッドの定義を簡略化できる`@Binds`が追加されました.  
今回はこれも使ってシンプルに行きましょう.  

```java
Bindsを使ったマップ定義をここに
```
