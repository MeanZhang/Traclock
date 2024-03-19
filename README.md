<img src="docs/images/logo.svg" alt="logo" width="100" height="100" align="right" />

# 时迹

> 一款记录任务时间的时间管理应用。

<p align="center">
  <a href="https://developer.android.google.cn/jetpack/compose" alt="Jetpack Compose">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-1.5.8-brightgreen?logo=android" />
  </a>
  <a href="https://android-arsenal.com/api?level=26" alt="API">
    <img src="https://img.shields.io/badge/API-26%2B-blue?logo=android" />
  </a>
  <a href="https://github.com/MeanZhang/Traclock/actions/workflows/android.yml"  alt="Android CI">
    <img src="https://github.com/MeanZhang/Traclock/actions/workflows/android.yml/badge.svg" />
  </a>
  <a href="https://github.com/MeanZhang/Traclock/releases"  alt="GitHub release">
    <img src="https://img.shields.io/github/v/release/MeanZhang/Traclock?sort=semver">
  </a>
</p>

## 简介

时迹一款记录各项任务时间的时间管理应用，可以帮助你了解自己的时间分配，提高工作效率。

使用 Jetpack Compose 构建，采用 Material You 设计，已完成项目记录、管理和统计等基本功能，UI 界面将随 Jetpack Compose 的更新逐步完善。

该应用将拥有以下特点：

- [x] 数据持久化保存，重启后可继续计时
- [x] 数据可备份和恢复
- [x] 适配深色模式
- [ ] 快捷方式（Shortcuts）
- [ ] 微件
- [ ] 数据统计
- [ ] ……

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

| 时间线                                                 | 时间线                                           | 项目                                     | 统计                                         |
| ------------------------------------------------------ | ------------------------------------------------ | ---------------------------------------- | -------------------------------------------- |
| ![timeline-detail](docs/images/ui/timeline-detail.png) | ![timeline](docs/images/ui/timeline.png)         | ![projects](docs/images/ui/projects.png) | ![statistics](docs/images/ui/statistics.png) |
| 设置                                                   | 通知                                             | 深色                                     | 恢复                                         |
| ![settings](docs/images/ui/settings.png)               | ![notification](docs/images/ui/notification.png) | ![dark](docs/images/ui/dark.png)         | ![restore](docs/images/ui/restore.png)       |
