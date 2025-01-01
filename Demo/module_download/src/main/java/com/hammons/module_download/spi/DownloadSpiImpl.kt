package com.hammons.module_download.spi

import com.xiaojinzi.component.anno.ServiceAnno
import com.xiaojinzi.component.base.spi.DownloadSpi
import com.xiaojinzi.component.base.spi.DownloadStatus
import com.xiaojinzi.component.impl.service.ServiceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


@ServiceAnno(value = [DownloadSpi::class, DownloadSpi::class], name = [ServiceManager.DEFAULT_NAME, "downloadSpi"])
class DownloadSpiImpl : DownloadSpi {
    override suspend fun startDownload(url: String): Flow<DownloadStatus> = flow {
        // 模拟下载任务
        println("开始下载 $url")
        for (i in 1..100) {
            val progress = i.toFloat() / 100
            emit(DownloadStatus.DownloadProgress(progress))
            Thread.sleep(100) // 模拟下载延迟
        }
        emit(DownloadStatus.DownloadSuccess)
    }.catch { exception ->
        emit(DownloadStatus.DownloadFailure(exception))
    }
    .flowOn(Dispatchers.IO)
}
