package com.galerkinrobotics.test.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // WebSocketManager artık @Singleton ve @Inject constructor ile otomatik sağlanıyor
}
