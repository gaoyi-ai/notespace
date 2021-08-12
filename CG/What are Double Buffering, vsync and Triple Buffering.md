---
title: What are Double Buffering, vsync and Triple Buffering?
categories:
- CG
- Double Buffering
tags:
- Buffering
date: 2021/8/11
---



> [What are Double Buffering, vsync and Triple Buffering? - Triple Buffering: Why We Love It (anandtech.com)](https://www.anandtech.com/show/2794/2)

### What are Double Buffering, vsync and Triple Buffering?

When a computer needs to display something on a monitor, it draws a picture of what the screen is supposed to look like and sends this picture (which we will call a buffer) out to the monitor. In the old days there was only one buffer and it was continually being both drawn to and sent to the monitor. There are some advantages to this approach, but there are also very large drawbacks. Most notably, when objects on the display were updated, they would often flicker.



![img](https://images.anandtech.com/reviews/video/triplebuffer/single.png)
*The computer draws in as the contents are sent out.
All illustrations courtesy Laura Wilson.*







In order to combat the issues with reading from while drawing to the same buffer, double buffering, at a minimum, is employed. The idea behind double buffering is that the computer only draws to one buffer (called the "back" buffer) and sends the other buffer (called the "front" buffer) to the screen. After the computer finishes drawing the back buffer, the program doing the drawing does something called a buffer "swap." This swap doesn't move anything: swap only changes the names of the two buffers: the front buffer becomes the back buffer and the back buffer becomes the front buffer.



![img](https://images.anandtech.com/reviews/video/triplebuffer/double.png)
*Computer draws to the back, monitor is sent the front.*







After a buffer swap, the software can start drawing to the new back buffer and the computer sends the new front buffer to the monitor until the next buffer swap happens. And all is well. Well, almost all anyway.

In this form of double buffering, a swap can happen anytime. That means that while the computer is sending data to the monitor, the swap can occur. When this happens, the rest of the screen is drawn according to what the new front buffer contains. If the new front buffer is different enough from the old front buffer, a visual artifact known as "tearing" can be seen. This type of problem can be seen often in high framerate FPS games when whipping around a corner as fast as possible. Because of the quick motion, every frame is very different, when a swap happens during drawing the discrepancy is large and can be distracting.

The most common approach to combat tearing is to wait to swap buffers until the monitor is ready for another image. The monitor is ready after it has fully drawn what was sent to it and the next vertical refresh cycle is about to start. **Sync**hronizing buffer swaps with the **V**ertical refresh is called vsync.

While enabling vsync does fix tearing, it also sets the internal framerate of the game to, at most, the refresh rate of the monitor (typically 60Hz for most LCD panels). This can hurt performance even if the game doesn't run at 60 frames per second as there will still be artificial delays added to effect synchronization. Performance can be cut nearly in half cases where every frame takes just a little longer than 16.67 ms (1/60th of a second). In such a case, frame rate would drop to 30 FPS despite the fact that the game should run at just under 60 FPS. The elimination of tearing and consistency of framerate, however, do contribute to an added smoothness that double buffering without vsync just can't deliver.

Input lag also becomes more of an issue with vsync enabled. This is because the artificial delay introduced increases the difference between when something actually happened (when the frame was drawn) and when it gets displayed on screen. Input lag always exists (it is impossible to instantaneously draw what is currently happening to the screen), but the trick is to minimize it.

Our options with double buffering are a choice between possible visual problems like tearing without vsync and an artificial delay that can negatively effect both performance and can increase input lag with vsync enabled. But not to worry, there is an option that combines the best of both worlds with no sacrifice in quality or actual performance. That option is triple buffering.

---

## Double buffering in computer graphics

In [computer graphics](https://en.wikipedia.org/wiki/Computer_graphics), **double buffering** is a technique for drawing graphics that shows no (or less) stutter, [tearing](https://en.wikipedia.org/wiki/Screen_tearing), and other artifacts.

It is difficult for a program to draw a display so that pixels do not change more than once. For instance, when updating a page of text, it is much easier to clear the entire page and then draw the letters than to somehow erase only the pixels that are used in old letters but not in new ones. However, this intermediate image is seen by the user as [flickering](https://en.wikipedia.org/wiki/Flicker_(screen)). In addition, [computer monitors](https://en.wikipedia.org/wiki/Computer_monitor) constantly redraw the visible video page (traditionally at around 60 times a second), so even a perfect update may be visible momentarily as a horizontal divider between the "new" image and the un-redrawn "old" image, known as [tearing](https://en.wikipedia.org/wiki/Page_tearing).

### Software double buffering

A software implementation of double buffering has all drawing operations store their results in some region of system [RAM](https://en.wikipedia.org/wiki/Random_Access_Memory); any such region is often called a "back buffer". When all drawing operations are considered complete, the whole region (or only the changed portion) is copied into the [video RAM](https://en.wikipedia.org/wiki/Video_RAM) (the "front buffer"); this copying is usually synchronized with the monitor's [raster](https://en.wikipedia.org/wiki/Raster_graphics) beam in order to avoid tearing. Software implementations of double buffering necessarily requires more memory and CPU time than single buffering because of the system memory allocated for the back buffer, the time for the copy operation, and the time waiting for synchronization.

[Compositing window managers](https://en.wikipedia.org/wiki/Compositing_window_manager) often combine the "copying" operation with "[compositing](https://en.wikipedia.org/wiki/Compositing)" used to position windows, transform them with scale or warping effects, and make portions transparent. Thus the "front buffer" may contain only the composite image seen on the screen, while there is a different "back buffer" for every window containing the non-composited image of the entire window contents.

### Page flipping

In the page-flip method, instead of copying the data, both buffers are capable of being displayed (both are in [Video RAM](https://en.wikipedia.org/wiki/Video_RAM)). At any one time, one buffer is actively being displayed by the monitor, while the other, background buffer is being drawn. When the background buffer is complete, the roles of the two are switched. The page-flip is typically accomplished by modifying a [hardware register](https://en.wikipedia.org/wiki/Hardware_register) in the [video display controller](https://en.wikipedia.org/wiki/Video_display_controller)—the value of a pointer to the beginning of the display data in the video memory.

The page-flip is much faster than copying the data and can guarantee that tearing will not be seen as long as the pages are switched over during the monitor's [vertical blanking interval](https://en.wikipedia.org/wiki/Vertical_blanking_interval)—the blank period when no video data is being drawn. The currently active and visible buffer is called the **front buffer**, while the background page is called the **back buffer**.

## Triple buffering

In [computer graphics](https://en.wikipedia.org/wiki/Computer_graphics), **triple buffering** is similar to double buffering but can provide improved performance. In double buffering, the program must wait until the finished drawing is copied or swapped before starting the next drawing. This waiting period could be several [milliseconds](https://en.wikipedia.org/wiki/Milliseconds) during which neither buffer can be touched.

In triple buffering the program has two back buffers and can immediately start drawing in the one that is not involved in such copying. The third buffer, the front buffer, is read by the graphics card to display the image on the monitor. Once the image has been sent to the monitor, the front buffer is flipped with (or copied from) the back buffer holding the most recent complete image. Since one of the back buffers is always complete, the graphics card never has to wait for the software to complete. Consequently, the software and the graphics card are completely independent and can run at their own pace. Finally, the displayed image was started without waiting for synchronization and thus with minimum lag.[[1\]](https://en.wikipedia.org/wiki/Multiple_buffering#cite_note-anandtech-triple-buffering-1)

Due to the software [algorithm](https://en.wikipedia.org/wiki/Algorithm) not polling the graphics hardware for monitor refresh events, the algorithm may continuously draw additional frames as fast as the hardware can render them. For frames that are completed much faster than interval between refreshes, it is possible to replace a back buffers' frames with newer iterations multiple times before copying. This means frames may be written to the back buffer that are never used at all before being overwritten by successive frames. Nvidia has implemented this method under the name "Fast Sync".[[2\]](https://en.wikipedia.org/wiki/Multiple_buffering#cite_note-2)

> It’s interesting to note that Fast Sync isn’t a wholly new idea, but rather a modern and more consistent take on an old idea: triple buffering. While in modern times triple buffering is just a 3-deep buffer that is run through as a sequential frame queue, in the days of yore some games and video cards handled triple buffering a bit differently. Rather than using the 3 buffers as a sequential queue, they would instead always overwrite the oldest buffer. This small change had a potentially significant impact on input lag, and if you’re familiar with old school triple buffering, then you know where this is going.
>
> [![img](https://images.anandtech.com/doci/10325/PascalEdDay_FINAL_NDA_1463156837-040_575px.png)](https://images.anandtech.com/doci/10325/PascalEdDay_FINAL_NDA_1463156837-040.png)

An alternative method sometimes referred to as triple buffering is a [swap chain](https://en.wikipedia.org/wiki/Swap_Chain) three buffers long. After the program has drawn both back buffers, it waits until the first one is placed on the screen, before drawing another back buffer (i.e. it is a 3-long [first in, first out](https://en.wikipedia.org/wiki/FIFO_(computing_and_electronics)) queue). Most Windows games seem to refer to this method when enabling triple buffering.

1.  ["Triple Buffering: Why We Love It"](http://www.anandtech.com/video/showdoc.aspx?i=3591&p=1). AnandTech. June 26, 2009.
2. Smith, Ryan. ["The NVIDIA GeForce GTX 1080 & GTX 1070 Founders Editions Review: Kicking Off the FinFET Generation"](http://www.anandtech.com/show/10325/the-nvidia-geforce-gtx-1080-and-1070-founders-edition-review/13).

