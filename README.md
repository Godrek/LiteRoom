# LiteRoom
Basic image processing application


#Currently in process of refactoring and moving code to C++
Features
------------------
Can load/save most image types, supports drag/drop for loading images

Can convert to individual R/G/B channels


**Histogram equalization:**

slider that controls the degree of equalization.

Higher values = closer to flat/more equal histogram



**Sobel filter:**

Checkbox for black or white outline

left # parameter is for thresholding

right # parameter is a scalar for magnitude values



**Grayscale/Color Gaussian Blur:**
parameter controls kernel dimensions, 
standard deviation is determined to best fit given dimensions

**Canny:**
left # parameter controls lower threshold

right # parameter controls upper threshold

runs sobel filter, calculates magnitudes and orientation ->

runs nonmaximum suppression to thin lines ->

classifies edges as non-edge, weak, strong ->

paints non-edge pixels black, weak pixels gray, strong pixels white



To-do
------------
Finish the double thresholding on Canny to promote weak edges to strong edges if connected to a strong edge


Notes
---------
Save tries to save as .png but sometimes the file has to have original image type suffix added
instead


