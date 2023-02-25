# FlySpring
Simple framework on top of Spring Boot. no bullshit   
 ▶ [Play with our starter on Replit](https://replit.com/@arakoodev/starter)

FlySpring is a gymnastic maneuver designed to build momentum and to launch the athlete.  
![Fly Spring](https://user-images.githubusercontent.com/76883/206560658-582a632f-bd32-4b0c-8359-ae2b9e49defe.png)

# FlyFly
`flyfly` is our magical cli. It supercharges your development cycle. Your spring code gets autorefreshed whenever you make a code change. instant reloads. Also sets up Testcontainers for you.
And does instantaneous code formatting for you, so your java code looks fresh and clean. Dont leave home without `flyfly`.

[![FlyFly cli from Arakoo FlySpring](https://user-images.githubusercontent.com/76883/210039417-6b513ed8-8a25-4e69-83c5-49ab05520c9d.jpg)](https://youtu.be/ZH86LTfjWtU)


## ⚔️ Kill The Decorator


## 🎊 Community

💫💫💫 **we would be very grateful if you could take 5 seconds to star our repository on Github. It helps get the word out to more Java developers & open source committers about FlySpring.** 💫💫💫

- Follow our [Twitter](https://twitter.com/arakoodev)
- Join  [support Discord](https://discord.gg/MtEPK9cnSF) to get help, or to chat with us!!!
- Open a [discussion](https://github.com/arakoodev/FlySpring/discussions/new) with your question, or
- Open [a bug](https://github.com/arakoodev/FlySpring/issues/new)
- Check open [Github Issues](https://github.com/arakoodev/FlySpring/issues)
- Make sure to read our [contributing CLA](https://github.com/arakoodev/.github/blob/main/CLA.md).

## 🪅 Getting Started

It's Spring ! Just two steps.
1. Install the `Spring-dependency/autoroute` plugin first. use `mvn install` inside the directory
2. After that, Run any example from `Examples` using maven.

## 🧐 Contributing Guidelines (There is only one)

This project hopes and requests for clean pull request merges. the way we merge is squash and merge. This fundamentally can only work if you **NEVER ISSUE A PULL REQUEST TWICE FROM THE SAME LOCAL BRANCH**. If you create another pull request from same local branch, then the merge will always fail.

solution is simple - **ONE BRANCH PER PULL REQUEST**. We Follow this strictly. if you have created this pull request using your master/main branch, then follow these steps to fix it:
```
# Note: Any changes not committed will be lost.
git branch newbranch      # Create a new branch, saving the desired commits
git checkout master       # checkout master, this is the place you want to go back
git reset --hard HEAD~3   # Move master back by 3 commits (Make sure you know how many commits you need to go back)
git checkout newbranch    # Go to the new branch that still has the desired commits. NOW CREATE A PULL REQUEST
```

## 💌 Acknowledgements

- First Hat tip to  [Spring](https://github.com/spring-projects/spring-framework).
- We are inspired by the spirit of [Nextjs](https://github.com/vercel/next.js/).
- All the other [contributors](https://github.com/wootzapp/wootz-browser/graphs/contributors).

## ✍️ Authors and Contributors

- Sandeep Srinivasa ([@sandys](https://twitter.com/sandeepssrin))

We love contributors! Feel free to contribute to this project but please read the [CLA](https://github.com/wootzapp/.github/blob/main/CLA.md) first!

<a href="https://github.com/arakoodev/FlySpring/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=arakoodev/FlySpring&max=300&columns=12&anon=0" />
</a>

## 📜 License

FlySpring is open-source OSS software licensed under the MIT license.

