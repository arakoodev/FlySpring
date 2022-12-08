# FlySpring
Simple framework on top of Spring Boot. no bullshit

FlySpring is a gymnastic maneuver designed to build momentum and to launch the athlete.  
![Fly Spring](https://user-images.githubusercontent.com/76883/206560658-582a632f-bd32-4b0c-8359-ae2b9e49defe.png)


## ⚔️ Kill The Wallet

- 👐 **Open Source**. We have the exact same license as Metamask
- 🎁 **No Extensions**. Just use the damn browser!
- 🙈 **Privacy First**. Not even ads.
- 🔐 **Cryptographic storage**. We use [Flutter Secure Store](https://pub.dev/packages/flutter_secure_storage) for secure storage on mobile. On Android and ios, it uses the native encrypted storage (like secure enclave)


## 🎊 Community

- Follow our [Twitter](https://twitter.com/arakoodev)
- Join  [support Discord](https://discord.gg/MtEPK9cnSF) to get help, or to chat with us!!!
- Open a [discussion](https://github.com/arakoodev/FlySpring/discussions/new) with your question, or
- Open [a bug](https://github.com/arakoodev/FlySpring/issues/new)
- Check open [Github Issues](https://github.com/arakoodev/FlySpring/issues)
- Make sure to read our [contributing CLA](https://github.com/arakoodev/.github/blob/main/CLA.md).

## 🪅 Getting Started

1. It's Spring !

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

