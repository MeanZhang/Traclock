<img src="docs/images/logo.svg" alt="logo" width="100" height="100" align="right" />

# 时迹

> 一款记录任务时间的跨平台时间管理应用。

<p align="center">
  <a href="https://www.jetbrains.com/zh-cn/lp/compose-multiplatform/" alt="Compose">
    <img src="https://img.shields.io/badge/Compose%20Multiplatform-gray?logo=jetpackcompose" />
  </a>
  <a href="https://android-arsenal.com/api?level=26" alt="API">
    <img src="https://img.shields.io/badge/API-26%2B-blue?logo=android" />
  </a>
  <a href="https://github.com/MeanZhang/Traclock/actions/workflows/android-build.yml"  alt="Android 构建">
    <img src="https://github.com/MeanZhang/Traclock/actions/workflows/android-build.yml/badge.svg" />
  </a>
  <a href="https://github.com/MeanZhang/Traclock/releases"  alt="GitHub Release">
    <img src="https://img.shields.io/github/v/release/MeanZhang/Traclock?sort=semver">
  </a>
</p>

## 简介

时迹是一款记录各项任务时间的跨平台时间管理应用，可以帮助你了解自己的时间分配，提高工作效率。

使用 Compose Multiplatform 构建，采用 Material You 设计，已完成项目记录、管理等基本功能。

该应用将拥有以下特点：

- [x] 数据持久化保存，重启后可继续计时
- [x] 数据可备份和恢复
- [x] 适配深色模式
- [ ] 快捷方式（Shortcuts）
- [ ] 微件
- [ ] 数据统计
- [ ] ……

## 平台支持

- Android
- Desktop

## 功能介绍

### 时间线

时间线按时间顺序展示所有记录，点击右上角切换图标可以切换普通视图和项目视图。

### 项目

显示所有项目，点击可查看项目所有记录或者修改项目信息。

### 统计

显示指定时间段（暂只支持当天）的统计数据。

### 设置

包括备份与恢复、帮助与反馈功能和关于页面。

## 截图

### 时间线

<div align="center">
    <img src="docs/images/ui/desktop-timeline.webp" width="64%"alt="桌面-时间线" />
    <img src="docs/images/ui/android-timeline.webp" width="23.2%" alt="Android-时间线" />
</div>

### 项目

<div align="center">
    <img src="docs/images/ui/desktop-projects.webp" width="64%"alt="桌面-项目" />
    <img src="docs/images/ui/android-projects.webp" width="23.2%" alt="Android-项目" />
</div>

### 统计

<div align="center">
    <img src="docs/images/ui/desktop-statistics.webp" width="64%"alt="桌面-s" />
    <img src="docs/images/ui/android-statistics.webp" width="23.2%" alt="Android-统计" />
</div>

### 设置

<div align="center">
    <img src="docs/images/ui/desktop-settings.webp" width="64%"alt="桌面-设置" />
    <img src="docs/images/ui/android-settings.webp" width="23.2%" alt="Android-设置" />
</div>

### 通知

<div align="center">
    <img src="docs/images/ui/desktop-tray-and-notification.webp" width="64%" alt="桌面-托盘和通知" />
    <img src="docs/images/ui/android-notification.webp" width="23.2%" alt="Android-通知" />
</div>
