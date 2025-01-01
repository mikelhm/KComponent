package com.xiaojinzi.component.base.spi

import kotlinx.coroutines.flow.Flow

interface DownloadSpi {
    suspend fun startDownload(url: String): Flow<DownloadStatus>
}

sealed class DownloadStatus {
    data class DownloadProgress(val progress: Float) : DownloadStatus()
    data object DownloadSuccess : DownloadStatus()
    data class DownloadFailure(val exception: Throwable) : DownloadStatus()
}