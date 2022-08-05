# Medium
https://workspace-dev.medium.com/jetpack-compose-textfield-5866bdae33e7

# APK
| app-snapshot-debug.apk | app-latest-debug.apk |
|:---:|:---:|
|![Screen_Recording_20220805-123550_Snapshot TextField_1](https://user-images.githubusercontent.com/7759511/183021094-dbfd74ee-74e8-4ccf-a602-25766d188c85.gif)|![Screen_Recording_20220805-033754_TextField Samsung Keyboard Problem_1](https://user-images.githubusercontent.com/7759511/183021005-4f100369-4f46-4d46-8370-10b394859502.gif)|

- app-snapshot-debug.apk (compose 1.3.0-SNAPSHOT, material3 1.0.0-SNAPSHOT)
- app-latest-debug.apk (compose 1.3.0-alpha02, material3 1.0.0-alpha15)


[apps.zip](https://github.com/holixfactory/TextField-SamsungKeyboard-Problem/files/9265878/apps.zip)

# 이전의 기록
<details>
<summary>내용 보기</summary>
# EditTextField

텍스트 입력을 받는 컴포넌트는 앱 개발 시 기본적으로 필요한 요소입니다. View에는 EditText, TextInputLayout등이 있었고, Jetpack Compose에는 BasicTextField, TextField가 있습니다.
Jetpack Compose는 Android 앱을 개발하는 새롭고 멋진 방법이지만, 치명적인 문제가 있습니다. 

<br />
그것은 바로 삼성키보드 '자소 분리' 이슈입니다. 

<br />
<br />
본 Repository는 Jetpack Compose의 TextField에 존재하는 한글 자소분리 문제를 쉽게 확인하고, 해결의 방향성을 제시하기 위하여 간략한 예시와 해결책을 담아 작성되었습니다. 

## 현존하는 문제점 및 재현방법
문제는 TextField를 아래와 같이 삼성 키보드의 문구추천 Option(설정 -> 일반 -> 삼성 키보드 설정 -> 문구 추천)이 켜져있는 상태로 사용할 경우 발생합니다.
<p align="center">
<img src="https://user-images.githubusercontent.com/64396971/181170422-3b505d1f-371c-4acd-950b-2ab934e81505.png" width="400">
</p>


재현 방법은 다음과 같습니다.

1. 한글로 여러 문자를 작성한다.

2. 커서의 위치를 지금까지 작성한 문자들 사이로 이동시킵니다.

3. 이어서 여러 문자를 타이핑합니다.

4. 이 경우 자음과 모음이 분리되고 직접 치지 않은 글자들까지 등장하게 됩니다.

<br/>

**아래 영상을 통해 좀 더 확실하게 확인할 수 있습니다.**

| 기존 Compose의 TextField | AndroidView를 사용한 CustomTextField |
|:---:|:---:|
| ![KakaoTalk_Photo_2022-07-27-13-49-48 001](https://user-images.githubusercontent.com/64396971/181163933-98d8c5e1-9f02-4fad-b5dc-b6bca4713342.gif)| ![KakaoTalk_Photo_2022-07-27-13-49-49 002](https://user-images.githubusercontent.com/64396971/181163936-eed9c460-0b1c-44fe-8380-dd7499f07434.gif)|

<br/>

## 해결을 위한 노력

### 1. 삼성 멤버스를 통한 삼성 키보드 앱에 대한 문의
이 문제를 처음으로 발견한 후 삼성 멤버스를 통해 제보를 했습니다. Gboard는 문제가 생기지 않아 키보드 앱 이슈가 아닐까 생각했습니다. 

이에 돌아온 답변은 다음과 같습니다.

> 답변 #1
> 고객님, 안녕하세요. 삼성 전자 휴대폰 담당입니다.
>
> 우선, 답변이 늦어진 점 대단히 죄송합니다.
> 보내주신 로그 분석 결과 Jetpack Compose Text field 에서 commit 수행 직후 `getTextBeforeCursor` 에 대한 리턴을 정상적으로 해주지 않은 것으로 보여집니다.
> 예를 들어 "Hellp" 입력 후 space 로 자동교체 동작시켜 "Hello" 가 실제로 입력될 경우 `getTextBeforeCursor` 는 "Hellp" 를 리턴하였고 "안녕" 이후에 'ㅇ' commit 할 경우 `getTextBeforeCursor` 가 "안녕ㅇ" 이 아닌 "안녕" 으로 리턴을 해준 것으로 보여집니다.
>
> 수행 순서로 보면 실제 입력 "안녕ㅇ", `getTextBeforeCursor` 는 "안녕" 리턴 하였고 이후에 'ㅏ' 입력 → 앞에 글자를 "안녕" 으로 리턴해주기 때문에 'ㅏ' 와 조합 시 "안녕ㅏ" 가 된 것으로 보여집니다.
>
> 또한 before text length 를 2글자("안녕") 로 잘 못 알게 되어 "안녕ㅇ하세요" 상황에서 before text length 2글자만큼 지우고 "안녕ㅏ" 를 결과적으로 "안안녕ㅏ하세요" 로 잘 못 입력된 것으로 보여집니다.
> 정상적으로 입력되는 경우 실제 입력 "안녕ㅇ", `getTextBeforeCursor` 는 "안녕ㅇ" 리턴되어 이후에 'ㅏ' 입력 → 앞에 글자를 "안녕ㅇ" 으로 리턴해주기 때문에 'ㅏ' 와 조합 시 "안녕아" 가 되며 또한 before text length 가 3글자("안녕ㅇ") "안녕ㅇ하세요" 상황에서 before text length 3글자만큼 지우고 "안녕아" 를 결과적으로 "안녕아하세요" 로 의도대로 정상 입력 됩니다.
>
> 외부 앱 키보드
> * Swiftkey 키보드
> "안녕하세요" 입력 후 space 띄어쓰기 이후 "안녕" "하세요" 사이로 커서 이동 시 다시 "안녕하세요" 뒤로 커서 자동변경 되는 이슈가 있습니다.
> * Gboard
> 문제가 발생하지 않음 → Gboard 는 reselection 을 지원하지 않아 입력 중인 글자로만 추천어를 만들어 줍니다.
> 사용에 참고 부탁드립니다.
> 감사합니다.

> 답변 #2 중복 제거하고 추가 사항
> 
> (중략)
> 구글에서 API를 공식적으로 만들게 되면 Device Vendor(Samsung과 같은)에게 `Android Compatibility Definition`을 통해서 API의 호환성 정의를 합니다. 그런데 이에 대해 Definition 요구가 없었습니다. 
> ...

<br />

### 2. 구글 Issue Tracker에 제보 

https://issuetracker.google.com/u/2/issues/195231205

Google 측에도 Issue Tracker를 통해 제보했습니다. 오랜 기간 답이 없다가, 영어 사용 시에도 비슷한 이슈가 생겼고 이 이슈가 함께 처리 되었습니다. 그러나, 온전하게 해결되지 않고 타이밍 이슈 같다는 의견과 함께 아무런 진전이 없는 상태입니다. 삼성 측으로부터 받은 답변을 전달했으나 추가 답변은 없었습니다.
<br />
### 3. AndroidView + EditText를 통한 TextField composable 구현

다행히 AndroidView를 통해 EditText를 사용할 수 있었습니다. 이런 식으로도 사용할 수 있다는 것을 보여주기 위해 [간단한 sample](https://github.com/holixfactory/EditTextField/blob/main/app/src/main/java/com/example/sampleedittext/CustomTextField.kt)을 작성해뒀습니다. 실제 저희 앱에서 쓰는 CustomTextField는 요구사항이 더 있어서 focus, styling등 부가적인 구현을 해둔 것이 있으니, 상황에 맞게 커스터마이징 하시면 될 것 같습니다!
<br />
#### 관련 이슈 (추정)
- https://issuetracker.google.com/issues/239471016
<br />
## 마치며
이 이슈는 Compose 출시 전 부터 현재 기준 최신 버전인 1.3.0-alpha01까지 해결되지 않은 문제입니다. 저희 서비스 내 운영 중인 [<Jetpack Compose 커뮤니티>](https://holix.com/c/OnwYAkw8)와 GDG 커뮤니티 등에서 이야기 한 적이 있었는데, 좀 더 본격적으로 함께 고민해보고자 이 repository를 준비했습니다. 

이미 많은 곳에서 Compose를 도입하신 것으로 알고 있는데, 이 이슈를 해결할 수 있는 방법이 있다면 github 이슈로 남겨주시거나, pr 부탁 드립니다.

<br />
감사합니다.
<br />
HOLIX Android Team
</details>
