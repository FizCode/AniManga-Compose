package dev.fizcode.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MalClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JikanClient
