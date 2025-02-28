package com.mshdabiola.designsystem.string

import androidx.compose.runtime.Composable
import hydraulic.modules.designsystem.generated.resources.Res
import hydraulic.modules.designsystem.generated.resources.app_name
import hydraulic.modules.designsystem.generated.resources.main_navigator
import hydraulic.modules.designsystem.generated.resources.modules_ui_cbt_exam_part
import hydraulic.modules.designsystem.generated.resources.modules_ui_cbt_sections
import hydraulic.modules.designsystem.generated.resources.modules_ui_cbt_subject
import hydraulic.modules.designsystem.generated.resources.modules_ui_cbt_type
import hydraulic.modules.designsystem.generated.resources.setting_navigator
import hydraulic.modules.designsystem.generated.resources.str_arr
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

val appName
    @Composable
    get() = stringResource(Res.string.app_name)

val subject
    @Composable
    get() = stringResource(Res.string.modules_ui_cbt_subject)

val type
    @Composable
    get() = stringResource(Res.string.modules_ui_cbt_type)

val examPart
    @Composable
    get() = stringArrayResource(Res.array.modules_ui_cbt_exam_part).toTypedArray()

val sections
    @Composable
    get() = stringArrayResource(Res.array.modules_ui_cbt_sections).toTypedArray()

@OptIn(ExperimentalResourceApi::class)
fun getFileUri(fileName: String) = Res.getUri(fileName)

@OptIn(ExperimentalResourceApi::class)
suspend fun getByte(fileName: String) = Res.readBytes(fileName)

val mainNavigator
    @Composable
    get() = stringArrayResource(Res.array.main_navigator)
val settingNavigator
    @Composable
    get() = stringArrayResource(Res.array.setting_navigator)

val cbtNavigator
    @Composable
    get() = stringArrayResource(Res.array.str_arr)
