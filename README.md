<h1 align="center">
  <br>
  <img src="https://raw.githubusercontent.com/StylexTV/UltimateHeads/main/showcase/socials/cover.png">
  <br>
</h1>

<h4 align="center">😊 Source code of the UltimateHeads spigot plugin, made with love in Java.</h4>

<p align="center">
  <a href="https://GitHub.com/StylexTV/UltimateHeads/stargazers/">
    <img alt="stars" src="https://img.shields.io/github/stars/StylexTV/UltimateHeads.svg?color=ffdd00"/>
  </a>
  <a href="https://www.codacy.com/manual/noluck942/GSigns?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=StylexTV/GSigns&amp;utm_campaign=Badge_Grade">
    <img alt="Codacy Badge" src="https://app.codacy.com/project/badge/Grade/a33dbb19ff17460d896a7864fececab6"/>
  </a>
  <a>
    <img alt="Code size" src="https://img.shields.io/github/languages/code-size/StylexTV/UltimateHeads.svg"/>
  </a>
  <a>
    <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/StylexTV/UltimateHeads.svg"/>
  </a>
  <a>
    <img alt="Lines of Code" src="https://tokei.rs/b1/github/StylexTV/UltimateHeads?category=code"/>
  </a>
</p>

## What is it?
UltimateHeads is a spigot plugin that allows you to access over 21,000 artistic heads.
> The spigotmc page, including a download link, can be found [here](https://www.spigotmc.org/resources/%E2%9A%A1-ultimateheads-%E2%9A%A1-an-enormous-head-collection-1-8-8-1-16-4.85797/).

## How does it work?
The UltimateHeads plugin is constantly connected to an `external database` containing all 21 thousand heads. The heads are regularly retrieved from this database and the plugin categorizes them. The plugin also combines these global heads with local heads (e.g. player heads) when the heads are displayed. When it comes to searching the heads, UltimateHeads uses the fast and optimized `TimSort` to display results in less than a second.

## Project Layout
Here you can see the current structure of the project.

```bash
├─ 📂 showcase/       # ✨ Showcase (eg. for spigot)
├─ 📂 src/            # 🌟 Source Files
│  ├─ 📂 assets/          # ✒️ Plugin Assets
│  │  └─ 📂 langs/        # 📦 Language Files
│  ├─ 📂 de/stylextv/     # ✉️ Source Code
│  └─ 📄 plugin.yml       # 📌 Plugin-YML
├─ 📂 version/        # 📬 Versions (used by the auto-updater)
└─ 📃 readme.md       # 📖 Read Me!
```
