# Spider Amulet

A Minecraft Fabric mod (1.20.1) that adds a craftable **Spider Amulet**. While you hold it in your hand (or wear it in your head/helmet slot):

- **Wall Climbing** — press against a wall (while airborne and not sneaking) to slowly climb it like a spider.
- **Ledge Leap** — sprint off a ledge to get a brief burst of forward speed, accompanied by a custom sound effect.

## Crafting Recipe

```
G R G
R S R
G R G
```

- **G** = Gold Ingot
- **R** = Redstone
- **S** = Spider Eye

## How to build the mod for FREE using GitHub (no Java install needed)

You do **not** need to install Java, Gradle, or anything on your computer. GitHub will build the `.jar` file for you automatically and for free.

Follow these steps exactly:

1. **Create a GitHub account** at https://github.com (free).
2. Click the **+** in the top-right → **New repository**. Give it any name (e.g. `spider-amulet`). Leave it public or private, then click **Create repository**.
3. On the new empty repository page, click the link **"uploading an existing file"**.
4. **Extract the ZIP** you downloaded (the one containing this project) so you get a normal folder.
5. **IMPORTANT (macOS users):** This project includes a hidden folder named `.github` that GitHub NEEDS to run the build. On macOS, folders starting with a dot are **invisible in Finder by default**. Open the extracted folder in Finder and press **Cmd + Shift + .** (period) to reveal hidden files. If you skip this step, the `.github` folder will not be uploaded, the build workflow will never run, and you will never get a `.jar` file.
6. Open the extracted folder and **select ALL files and folders from INSIDE it** — including the now-visible `.github` folder. Do **NOT** drag the outer folder itself; drag its **contents**.
7. Drag all those selected items into the GitHub upload area in your browser.
8. Scroll down and click **Commit changes**.
9. Click the **Actions** tab at the top of your repository. You will see a build running.
10. Wait about **2 minutes** for it to finish (green checkmark).
11. Click into the completed build run, scroll down to **Artifacts**, and download **mod-jar**.
12. Unzip the downloaded artifact to get the `.jar` file.
13. Copy that `.jar` into your Minecraft `mods` folder:
    - Windows: `%appdata%\.minecraft\mods`
    - macOS: `~/Library/Application Support/minecraft/mods`
    - Linux: `~/.minecraft/mods`
14. Make sure you have **Fabric Loader** and **Fabric API** installed for Minecraft **1.20.1**, then launch the game.

Enjoy climbing walls like a spider!

## Custom Sound

The mod triggers a custom sound event `spideramulet:leap` when you perform a ledge leap. A placeholder sound file path (`assets/spideramulet/sounds/leap.ogg`) is referenced in `sounds.json`. If you want an actual audio file, add your own `leap.ogg` at `src/main/resources/assets/spideramulet/sounds/leap.ogg` before building. The mod will still build and run without the file (the sound simply won't play audio, but the event still fires).