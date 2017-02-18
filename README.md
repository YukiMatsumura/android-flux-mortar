# SIMPLE FLUX, TINY MORTAR, SWEET DAGGER2

Androidでは画面回転によってアクティビティが破棄・再生成されます.  
よくある問題で, データに期待するライフサイクルよりアクティビティのライフサイクルの方が短いため, インスタンスの保存と復元が必要になります.  
Androidには標準で`savedInstanceState`の仕組みが用意されていますが, 処理中のバックグラウンドタスクの扱いや非同期メッセージの受信, 複雑なユーザインタフェースを適切に復元することなど, 悩ましい問題が多くあります.  

この問題を解決する案として下記がうまく動作しているため本リポジトリに書き溜めていきます.  

 - FluxのViewはStoreのObserverであるルールでユーザインタフェースを構築/復元
 - StoreのライフサイクルをMortarで管理
 - スコープの定義をDaggerで宣言

## DAGGER2 

本アプリでは, Dagger2のアクティビティコンポーネントをサブコンポーネントとして定義します.  
また, サブコンポーネントは[マルチバインディング機能](https://google.github.io/dagger/multibindings.html)を使って親コンポーネントを汚さない工夫をしてあります.  

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

