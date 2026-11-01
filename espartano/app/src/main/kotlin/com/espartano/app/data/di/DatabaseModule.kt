package com.espartano.app.data.di

import android.content.Context
import androidx.room.Room
import com.espartano.app.data.db.EspartanoDatabase
import com.espartano.app.data.db.dao.EntrenamientoDao
import com.espartano.app.data.db.dao.MedicionDao
import com.espartano.app.data.db.dao.PerfilDao
import com.espartano.app.data.db.dao.RutinaDao
import com.espartano.app.data.db.dao.SesionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EspartanoDatabase =
        Room.databaseBuilder(context, EspartanoDatabase::class.java, "espartano.db")
            .addMigrations(
                EspartanoDatabase.MIGRATION_1_2,
                EspartanoDatabase.MIGRATION_2_3,
                EspartanoDatabase.MIGRATION_3_4
            )
            .build()

    @Provides fun providePerfilDao(db: EspartanoDatabase): PerfilDao = db.perfilDao()
    @Provides fun provideMedicionDao(db: EspartanoDatabase): MedicionDao = db.medicionDao()
    @Provides fun provideEntrenamientoDao(db: EspartanoDatabase): EntrenamientoDao = db.entrenamientoDao()
    @Provides fun provideRutinaDao(db: EspartanoDatabase): RutinaDao = db.rutinaDao()
    @Provides fun provideSesionDao(db: EspartanoDatabase): SesionDao = db.sesionDao()
}
