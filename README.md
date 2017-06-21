# ThreadingRobot
### A framework to install that prevents the need to copy and paste complicated threading and networking code repeatedly.

When subclassing ThreadingRobot, the average code length is **less than half** of the code length when sublcassing IterativeRobot.

### Instructions

#### Mac
1.  Click [here](https://github.com/Tino-FRC-2473/ThreadingRobot/raw/master/ThreadingRobot.jar "Releases") to download the JAR file.
2. Open Terminal
3. Type in "cd" and press enter.
4. Type in "cd wpilib/user/java/lib" and press enter.
5. Type in "open ." and press enter.
6. Take the JAR file that you downloaded and drop it in the window that popped up.
7. Restart Eclipse.

#### Windows
1.  Click [here](https://github.com/Tino-FRC-2473/ThreadingRobot/raw/master/ThreadingRobot.jar "Releases") to download the JAR file.
2. Open Command Prompt
3. Type in "cd C:\Users\username\wpilib\user\java\lib" and press enter. **Replace "username" with the name of the user you are currently logged in to.**
4. Type in "start ." and press enter.
5. Take the JAR file that you downloaded and drop it in the window that popped up.
7. Restart Eclipse.

### Features
- Massively reduced code size
- Built in networking and threading support
- Able to run on any roboRIO centered system, from a single motor to a full robot
- Increased readability of code
- Increased ease of implementation

### Roadmap
#### Version 2.0
- Method access limited only to used methods, rest are package private, or protected.
- Increased error handling and reporting.
- API docs for framework developers.
- BooleanSupplier listener support
- StringSupplier listener support
- More convenient naming of parameters.

#### Version 3.0
- Implement interfaces in architecture to promote use of certain methods.
