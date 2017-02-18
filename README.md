# 🎩 SIMPLE FLUX, TINY MORTAR, SWEET DAGGER2

[![Goto Blog](https://github.com/YukiMatsumura/android-flux-mortar/blob/master/art/gotoblog.png?raw=true)](http://yuki312.blogspot.jp/) [![CircleCI](https://circleci.com/gh/YukiMatsumura/android-flux-mortar.svg?style=svg)](https://circleci.com/gh/YukiMatsumura/android-flux-mortar)  


## 🔰 WHAT'S THIS ?

Androidでは画面回転によってアクティビティが破棄・再生成されます.  
よくある問題で, データに期待するライフサイクルよりアクティビティのライフサイクルの方が短いため, インスタンスの保存と復元が必要になります.  
Androidには標準で`savedInstanceState`の仕組みが用意されていますが, 処理中のバックグラウンドタスクの扱いや非同期メッセージの受信, 複雑なユーザインタフェースを適切に復元することなど, 悩ましい問題が多くあります.  

この問題を解決する案として下記がうまく動作しているため本リポジトリに書き溜めていきます.  

 - スコープの定義をDaggerで宣言
 - FluxのViewはStoreのObserverであるルールでユーザインタフェースを構築/復元
 - StoreのライフサイクルをMortarで管理

## 🗡 DAGGER2

[https://google.github.io/dagger/](https://google.github.io/dagger/)

本アプリではDagger2を使ったDIと, オブジェクトのスコープ管理をしています.  

アクティビティコンポーネントはサブコンポーネントとして定義しており,
サブコンポーネントは[マルチバインディング機能](https://google.github.io/dagger/multibindings.html)を使ってコンポーネントビルダーのマップを自動生成することで, 親コンポーネントを汚さない工夫をしてあります.  
そのため, Daggerのバージョンは2.7以上が必要ですが, マルチバインディングを使用しない場合はこの限りではありません.  

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

// 依存性を注入
component.injectMembers(activity);
```

さらに詳しい情報は下記のブログをご覧ください.  

 - [Dagger2. MultibindingでComponentを綺麗に仕上げる](http://yuki312.blogspot.jp/2017/02/dagger2-multibindingcomponent.html)


## 🍣 FLUX

[https://facebook.github.io/flux/docs/overview.html](https://facebook.github.io/flux/docs/overview.html)  

ビューの状態管理は複雑化しやすく, アクティビティやフラグメントは肥大化しやすい傾向にあります.  
これに加えて, アクティビティは固有のライフサイクルを持っているため状態の保存と復元を考える必要があります.  

Fluxアーキテクチャはストアがビューの状態を一元管理するため, アクティビティはストアのオブサーバに徹することでビューの状態管理から解放され, ユーザインタフェース（アクティビティ）とデータ（ストア）のライフサイクルを切り離して考えることができるようになります.  

📝 執筆中...


## 🔩 MORTAR

[https://github.com/square/mortar](https://github.com/square/mortar)  

アクティビティが問題を複雑にしている点は, ユーザがタスクを終えるまでの時間と比べてライフサイクルが短命であることです.  
例えば, 特に対策もない場合にスクリーンを回転されることでアクティビティは再生成されます.  
スクリーンの回転でユーザ体験を損ねないようにするためには, ライフサイクルを跨ぐデータの延命措置が必要です.  

Mortarはそんなアクティビティのライフサイクルの隙間を埋めるために重宝します.  
本来, ビューとコントローラ（プレゼンター）のペアを構築する土台も提供していますが, 本アプリでは主にスコープを制御する`MortarScope`のためだけに導入しています.  

📝 執筆中...


## 📚 USE LIBRARY

 - [ReactiveX/RxJava](https://github.com/ReactiveX/RxJava)
 - [trello/RxLifecycle](https://github.com/trello/RxLifecycle/tree/2.x)
 - [square/mortar](https://github.com/square/mortar)
 - [rejasupotaro/kvs-schema](https://github.com/rejasupotaro/kvs-schema)
 - [google/dagger](https://github.com/google/dagger)
 - [evant/gradle-retrolambda](https://github.com/evant/gradle-retrolambda)
