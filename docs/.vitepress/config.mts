import { defineConfig } from "vitepress";
import taskLists from "markdown-it-task-lists";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  lang: "zh-CN",
  title: "时迹",
  description: "一款记录任务时间的时间管理应用",
  head: [
    ["link", { rel: "icon", href: "/images/logo.svg" }],
    ["link", { rel: "manifest", href: "/manifest.webmanifest" }],
    ["meta", { name: "theme-color", content: "#3eaf7c" }],
    ["meta", { name: "apple-mobile-web-app-capable", content: "yes" }],
    [
      "meta",
      { name: "apple-mobile-web-app-status-bar-style", content: "black" },
    ],
    [
      "link",
      {
        rel: "apple-touch-icon",
        href: "/images/icons/apple-touch-icon-152x152.png",
      },
    ],
    ["link", { rel: "mask-icon", href: "/images/logo.svg", color: "#5bbad5" }],
    ["meta", { name: "msapplication-TileColor", content: "#2d89ef" }],
  ],
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    logo: { light: "/images/logo.svg", dark: "/images/logo-dark.svg" },
    nav: [
      {
        text: "使用帮助",
        link: "/help/index",
      },
      {
        text: "隐私政策",
        link: "/privacy",
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/MeanZhang/Traclock" },
    ],
    footer: {
      copyright: "Copyright © 2022-2024 Mean",
    },
    search: {
      provider: "local",
    },
  },
  markdown: {
    config: (md) => {
      md.use(taskLists);
    },
  },
});
