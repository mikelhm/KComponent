package com.xiaojinzi.component.app1.module.main.view

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.xiaojinzi.component.base.spi.DownloadSpi
import com.xiaojinzi.component.base.spi.DownloadStatus
import com.xiaojinzi.component.base.RouterConfig
import com.xiaojinzi.component.base.api.RouterApi
import com.xiaojinzi.component.base.view.ActionButton
import com.xiaojinzi.component.base.view.AppbarNormal
import com.xiaojinzi.component.app1.module.main.domain.MainUseCase
import com.xiaojinzi.component.base.spi.UserSpi
import com.xiaojinzi.component.impl.Router
import com.xiaojinzi.component.impl.application.ModuleManager
import com.xiaojinzi.component.impl.routeApi
import com.xiaojinzi.component.impl.service.service
import com.xiaojinzi.support.ktx.nothing
import com.xiaojinzi.support.ktx.toStringItemDto
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
private fun MainView() {
    val context = LocalContext.current
    val vm: MainViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nothing(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .nothing(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 2.dp,
        ) {

            ActionButton(text = "加载 app 模块") {
                ModuleManager.register(
                    moduleName = "app",
                )
            }

            ActionButton(text = "卸载 app 模块") {
                ModuleManager.unregister(
                    moduleName = "app",
                )
            }

            ActionButton(text = "加载 support 模块") {
                ModuleManager.register(
                    moduleName = "support",
                )
            }

            ActionButton(text = "卸载 support 模块") {
                ModuleManager.unregister(
                    moduleName = "support",
                )
            }

            ActionButton(text = "加载 user 模块") {
                ModuleManager.register(
                    moduleName = "user",
                )
            }

            ActionButton(text = "卸载 user 模块") {
                ModuleManager.unregister(
                    moduleName = "user",
                )
            }

            ActionButton(text = "去测试路由") {
                Router.with(context = context)
                    .hostAndPath(hostAndPath = RouterConfig.APP_TEST_ROUTE)
                    .forward()
            }

        }
            Button(
                onClick = {

                    coroutineScope.launch {
                        Log.d("lhmtesting", "Download" + DownloadSpi::class.service())
                        val flow = DownloadSpi::class.service()!!.startDownload("")
                        flow.collect { status ->
                            when (status) {
                                is DownloadStatus.DownloadProgress -> Log.d("lhmtesting", "Download progress: ${status.progress}")
                                is DownloadStatus.DownloadSuccess -> Log.d("lhmtesting","Download completed successfully")
                                is DownloadStatus.DownloadFailure -> Log.d("lhmtesting","Download failed: ${status.exception}")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "测试 下载组件")
            }


        ActionButton(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth()
                .nothing(),
            text = "去 App 详情",
        ) {

            RouterApi::class
                .routeApi()
                .toAppDetail(context = context)

        }

        ActionButton(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth()
                .nothing(),
            text = "去网页测试跳转",
        ) {
            RouterApi::class
                .routeApi()
                .toWebTestView1()
        }

        ActionButton(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth()
                .nothing(),
            text = "通知栏跳转1",
        ) {

            val intent = Router.newProxyIntentBuilder()
                .hostAndPath(hostAndPath = RouterConfig.SYSTEM_CALL_PHONE)
                .putString(key = "tel", value = "18888888888")
                .buildProxyIntent()
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context, MainUseCase.CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(com.xiaojinzi.component.app1.R.mipmap.ic_launcher_round)
                .setContentTitle("测试点击跳转")
                .setContentText("使用默认代理Activity, 点我跳转到电话界面, 自动处理权限问题")
                .setContentIntent(
                    PendingIntent.getActivity(
                        context.applicationContext,
                        0,
                        intent,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PendingIntent.FLAG_IMMUTABLE
                        } else {
                            0
                        },
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager.notify(1, builder.build())
        }

        ActionButton(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth()
                .nothing(),
            text = "通知栏跳转2",
        ) {

            val intent = Router.newProxyIntentBuilder()
                .hostAndPath(hostAndPath = RouterConfig.SYSTEM_CALL_PHONE)
                .putString(key = "tel", value = "18888888888")
                .proxyActivity(clazz = MainAct::class)
                .buildProxyIntent()
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context, MainUseCase.CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(com.xiaojinzi.component.app1.R.mipmap.ic_launcher_round)
                .setContentTitle("测试点击跳转")
                .setContentText("使用默认代理Activity, 点我跳转到电话界面, 自动处理权限问题")
                .setContentIntent(
                    PendingIntent.getActivity(
                        context.applicationContext,
                        0,
                        intent,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PendingIntent.FLAG_IMMUTABLE
                        } else {
                            0
                        },
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager.notify(2, builder.build())
        }

        Spacer(
            modifier = Modifier
                .height(height = 120.dp)
                .nothing()
        )

        Spacer(
            modifier = Modifier
                .height(height = 120.dp)
                .nothing()
        )

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun MainViewWrap() {
    Scaffold(
        topBar = {
            AppbarNormal(
                backIconRsd = null,
                title = "KComponent".toStringItemDto(),
            )
        }
    ) {
        MainView()
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Preview
@Composable
private fun MainViewPreview() {
    MainView()
}