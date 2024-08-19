import os
import argparse


def getVersion():
    """
    从 `build.gradle` 或 `build.gradle.kts` 中获取版本号
    """
    gradle_file = os.path.join(os.getcwd(), "composeApp", "build.gradle")
    if not os.path.exists(gradle_file):
        gradle_file = os.path.join(os.getcwd(), "composeApp", "build.gradle.kts")
    with open(gradle_file, "r", encoding="utf-8") as f:
        for line in f.readlines():
            if "versionName" in line:
                return line.split('"')[1]


def renameApks():
    """
    重命名 apk 文件
    """
    apk_path = os.path.join(os.getcwd(), "composeApp", "build", "outputs", "apk", "release")
    print(f"apk_path: {apk_path}")
    apk_files = [file for file in os.listdir(apk_path) if file.endswith(".apk")]
    version = getVersion()
    for apk in apk_files:
        new_apk_name = apk.replace("composeApp", f"{os.path.basename(os.getcwd())}-v{version}")
        os.rename(os.path.join(apk_path, apk), os.path.join(apk_path, new_apk_name))
        print(f"Rename {apk} to {new_apk_name}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="CI Tools")
    parser.add_argument("tool", help="CI Tools", choices=["rename"])
    args = parser.parse_args()
    if args.tool == "rename":
        renameApks()
