package com.example.avitotest.shared.data.services

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.writeToFile
import aws.smithy.kotlin.runtime.http.engine.okhttp.OkHttpEngine
import aws.smithy.kotlin.runtime.net.url.Url
import com.example.avitotest.BuildConfig
import com.example.avitotest.shared.domain.datasource.StorageDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YandexCloudService @Inject constructor(
    @param:ApplicationContext private val context: Context
) : StorageDataSource {

    private val s3Client: S3Client by lazy {
        S3Client {
            region = "ru-central1"
            endpointUrl = Url.parse("https://storage.yandexcloud.net")

            forcePathStyle = true

            credentialsProvider = StaticCredentialsProvider(
                Credentials(
                    accessKeyId = BuildConfig.YANDEX_ACCESS_KEY,
                    secretAccessKey = BuildConfig.YANDEX_SECRET_KEY
                )
            )

            httpClient = OkHttpEngine()
        }
    }

    override suspend fun uploadFile(uriString: String, key: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(uriString.toUri())
                    ?: return@withContext Result.failure(Exception("Failed to open InputStream from Uri"))

                val bytes = inputStream.readBytes()

                val request = PutObjectRequest {
                    bucket = BuildConfig.YANDEX_BUCKET_NAME
                    this.key = key
                    body = ByteStream.fromBytes(bytes)
                }

                s3Client.putObject(request)

                val fileUrl = "https://${BuildConfig.YANDEX_BUCKET_NAME}.storage.yandexcloud.net/$key"
                Result.success(fileUrl)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }

    override suspend fun downloadFile(key: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val destinationFile = File.createTempFile(key, "download", context.cacheDir)

            s3Client.getObject(GetObjectRequest {
                bucket = BuildConfig.YANDEX_BUCKET_NAME
                this.key = key
            }) { response ->
                // A non-null assertion is safe here if the object is expected to exist.
                response.body!!.writeToFile(destinationFile)
            }

            Result.success(destinationFile)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
