# Some sample code for a clean architecture integration of amazon in app payments

## to run sample

- install the amazon app store and amazon app tester on device [click this link from your device](https://www.amazon.com/Amazon-App-Tester/dp/B00BN3YZM2/)
- push the product definitions to the device: `adb push consumable-iap.json /sdcard/amazon.sdktester.json`
- set the device to sandbox mode: `adb shell setprop debug.amazon.sandboxmode debug`
- for testing, use debug variant only


## module structure

![module structure](architecture.png)

- domain is implemented as a pure kotlin module
- domain can see no other modules
- app module is used for DI and that's about it
- app module is the only module that can see all other modules

## logs
logs are visible under the filter: "amzn_"


## License

    Copyright 2015-2022 early.co

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
