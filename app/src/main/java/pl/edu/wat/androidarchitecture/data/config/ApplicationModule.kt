package pl.edu.wat.androidarchitecture.data.config

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.edu.wat.androidarchitecture.data.repository.remote.SheetApi
import pl.edu.wat.androidarchitecture.data.service.ConnectionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideCharacterDao(db: ApplicationDatabase) = db.sheetDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = ApplicationDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideSheetApi(retrofit: Retrofit): SheetApi = retrofit.create(SheetApi::class.java)

    @Singleton
    @Provides
    fun provideApi(httpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://51.83.134.180:8080/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()

    @Singleton
    @Provides
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideConnectionManager() = ConnectionManager()
}
