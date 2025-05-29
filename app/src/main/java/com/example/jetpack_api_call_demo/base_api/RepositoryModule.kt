package  com.jetpack_demo.base_api

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
}