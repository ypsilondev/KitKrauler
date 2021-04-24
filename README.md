# KitKrauler
### Simple web driver to receive KIT Campus System notifications.

## How to use - Setup
### 1. Download your browsers web driver
* Microsoft Edge [Download-Site](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
* FireFox [Download-Site](https://github.com/mozilla/geckodriver/releases)

### 2. Download KitKrauler
See [Releases](https://github.com/ypsilondev/KitKrauler/releases)

### 3. Store KitKrauler and your browsers web driver in the same directory
It's important to store the KitKrauler .jar and your browsers web driver in the same directory

## How to use - Receive notifications
### Console-Use
1. Open a Terminal in the directory where the .jar and webdriver is stored
2. Type ``java -jar -Dengine=edge kitkrauler.jar U_KÜRZEL PASSWORD`` for Windows systems or ``java -jar -Dengine=firefox kitkrauler.jar U_KÜRZEL PASSWORD`` for Mac/Linux systems and replace U_KÜRZEL with your KIT U-Kürzel and PASSWORD with your KIT Account password.
3. The KitKrauler will silently retrieve Campus Portal updates in the background without opening the browser directly
You will receive notifications on your PC when updates happened.

### Graphical-Use
1. Open a Terminal in the directory where the .jar and webdriver is stored
2. Type ``java -jar -Dengine=edge kitkrauler.jar`` for MS Edge or ``java -jar -Dengine=firefox kitkrauler.jar`` for Firefox
3. The web browser will open and ask you to login on the KIT Shibboleth Login site
4. After a successful login, the KitKrauler will navigate to the Exams site and will contanstly lookup for updates
You will receive notifications on your PC when updates happened.

## Browser supported
Currently supported are (for both Graphical and Console based use)
* Microsoft Edge (Windows, MacOS, Linux)
* FireFox (Windows, MacOS, Linux)
