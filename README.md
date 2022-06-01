# FrameworkInjector

![](https://cdn.jsdelivr.net/gh/NasdaqGodzilla/PeacePicture/img/logo_frameworkInjector.png)

[中文说明](https://github.com/NasdaqGodzilla/FrameworkInjector/blob/develop/README_CN.md)

`FrameworkInjector` is an AOP framework for Java which is managing pointcut configurations by json and performing code weaving with `Javassist`.

_At the begining, `FrameworkInjector` is designed for AOP code weaving of Android framework. This is the origin of the name._

# Feature
1. Managing AOP pointcut configurations by JSON
2. Perform code weaving on specified Java class method
3. Support wildcard while matching class and method name
4. [BUG][TODO] Skipping interface class so that interface currently would not perform code weaving.

# Setup
```
source build.sh
```

Run `build.sh` to compile by `javac` and package by `jar`. The final target is runnable jar.

# Usage
The runnable jar of this program support parameters:
1. `--pointcuts_json` Sets path of json that managing pointcut configurations
2. `-i` Sets the input jar file which performing weaving from
3. `-o` Sets the output jar file which is been injected
4. `-cplist` Class Path List for Javassist. In case of class that exists in the input jar is not able to found in system class path, passing the path of the class to Javassist with this params to help Javassist load it. Every path is concat by ':'(Check it on JDK specifications).

## Run the sample
```
source build_and_run_sample.sh
```

`build_and_run_sample.sh` contains a lot of steps. It will make this program by calling `build.sh`. And then runs the inside sample(`sample/sample.json`). The sample will make this program perform code weaving on the jar generated by `build.sh`

## Run

Perform code weaving on foo.jar:

```
java -jar frameworkinjector.jar -cplist "libs" -pointcuts_json myPointcuts.json -i foo.jar -o injected_foo.jar
```

When error like "Class NotFound" happends, it is useful that add class path:

```
java -Xbootclasspath/a:/path/myPath:<other> <...other cmd parts...>
```

# How it works

![](https://cdn.jsdelivr.net/gh/NasdaqGodzilla/PeacePicture/img/FrameworkInjector架构.drawio.png)

- Perform code weaving with [Javasist](https://github.com/jboss-javassist/javassist)
- Parse json with [Jackson](https://github.com/FasterXML/jackson)

# Sample
1. Run `build_and_run_sample.sh`, which will perform code weaving configed by [sample.json](https://github.com/NasdaqGodzilla/FrameworkInjector/blob/main/sample/sample.json)
2. A new jar that is injected will be produced from step 1. It containes codes injected by this program. In the case of the sample, a new `System.out.println` call is injected which shown as below.

![](https://cdn.jsdelivr.net/gh/NasdaqGodzilla/PeacePicture/img/FrameworkInject_Wildcard_result.png)

# LICENSE
Copyright (C) <2022> <copyright Niko Zhong>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
