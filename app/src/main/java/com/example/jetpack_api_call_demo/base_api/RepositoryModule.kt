package  com.jetpack_demo.base_api

import com.example.jetpack_api_call_demo.repo.AddClientRepository
import com.jetpack_demo.base_api.ApiServices
import com.jetpack_demo.repo.ClientListRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideResendOtpRepository(apiService: ApiServices, moshi: Moshi) =
        ClientListRepository(apiService, moshi)

    @Singleton
    @Provides
    fun provideAddClientRepository(apiService: ApiServices, moshi: Moshi, ) =
        AddClientRepository(apiService, moshi)
}