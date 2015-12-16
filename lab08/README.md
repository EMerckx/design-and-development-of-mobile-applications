# Lab 8: Responsive webdesign: Bootstrap

* Bootstrap: OK
* Mobile: OK
* Manifest: OK
* Homescreen: OK
* Custom bootstrap: OK
* Appcache: OK
* Offline usage: OK

## Question 1

Why can it be that, while using the full standard version, the loading time of Bootstrap is smaller than the loading time while using a custom version of Bootstrap. Even if we only need a small piece of the functionality?

```
The standard version is used by many other sites. 
And it gets delivered by a CDN, which is a fast server.
So there is a big chance that the full version is already present,
in the cache of the browser.
```