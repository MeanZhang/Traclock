import { defaultTheme, defineUserConfig } from "vuepress";

export default defineUserConfig({
  lang: "zh-CN",
  title: "时迹",
  description: "时迹帮助文档",
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

  theme: defaultTheme({
    logo: "/images/logo.svg",
    repo: "MeanZhang/Traclock",
    docsBranch: "main",
    docsDir: "docs",
    editLinkText: "编辑此页面",
    lastUpdatedText: "上次更新于",
    contributorsText: "贡献者",
    navbar: [
      {
        text: "使用帮助",
        link: "/help",
      },
      {
        text: "隐私政策",
        link: "/privacy",
      },
    ],
  }),

  // plugins: [
  //     [
  //         '@vuepress/pwa',
  //         {
  //             skipWaiting: true,
  //         },
  //     ],
  // ],
});
