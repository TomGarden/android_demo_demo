package io.github.tomgarden.kotlincoroutine;

import javax.inject.Singleton;

import dagger.Component;
import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.ComponentEntryPoint;
import okhttp3.OkHttpClient;

//@Singleton
//@Component(modules = InstanceModule.class)
//public interface AppComponent {
//    void inject(OtherObject otherObject);
//}

@Component(modules = InstanceModule.class)
public interface AppComponent {
    void inject(OtherObject otherObject);
}
