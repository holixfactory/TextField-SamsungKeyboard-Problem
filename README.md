# EditTextField

텍스트 입력을 받는 컴포넌트는 앱 개발 시 기본적으로 필요한 요소입니다. View에는 EditText가 있었고, Compo
본 Repository는 Android Jetpack Compose의 Textfield에 존재하는 한글 자소분리 문제를 쉽게 확인하고, 해결의 방향성을 제시하기 위하여 간략한 예시와 해결책을 담아 작성되었습니다.

## 현존하는 문제점 및 재현방법
문제는 Compose TextField를 아래와 같이 삼성 키보드의 문구추천 Option(설정 -> 일반 -> 삼성 키보드 설정 -> 문구 추천)이 켜져있는 상태로 사용할 경우 발생합니다.
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
이 문제를 처음으로 발견한 후 삼성측에 제보를 했습니다. 이에 돌아온 답변은 다음과 같습니다.

> 고객님, 안녕하세요. 삼성 전자 휴대폰 담당입니다.

> 우선, 답변이 늦어진 점 대단히 죄송합니다.
> 보내주신 로그 분석 결과 Jetpack Compose Text field 에서 commit 수행 직후 getTextBeforeCursor 에 대한 리턴을 정상적으로 해주지 않은 것으로 보여집니다.
> 예를 들어 "Hellp" 입력 후 space 로 자동교체 동작시켜 "Hello" 가 실제로 입력될 경우 getTextBeforeCursor 는 "Hellp" 를 리턴하였고 "안녕" 이후에 'ㅇ' commit 할 경우 getTextBeforeCursor 가 "안녕ㅇ" 이 아닌 "안녕" 으로 리턴을 해준 것으로 보여집니다.

> 수행 순서로 보면 실제 입력 "안녕ㅇ", getTextBeforeCursor 는 "안녕" 리턴 하였고 이후에 'ㅏ' 입력 → 앞에 글자를 "안녕" 으로 리턴해주기 때문에 'ㅏ' 와 조합 시 "안녕ㅏ" 가 된 것으로 보여집니다.

> 또한 before text length 를 2글자("안녕") 로 잘 못 알게 되어 "안녕ㅇ하세요" 상황에서 before text length 2글자만큼 지우고 "안녕ㅏ" 를 결과적으로 "안안녕ㅏ하세요" 로 잘 못 입력된 것으로 보여집니다.
> 정상적으로 입력되는 경우 실제 입력 "안녕ㅇ", getTextBeforeCursor 는 "안녕ㅇ" 리턴되어 이후에 'ㅏ' 입력 → 앞에 글자를 "안녕ㅇ" 으로 리턴해주기 때문에 'ㅏ' 와 조합 시 "안녕아" 가 되며 또한 before text length 가 3글자("안녕ㅇ") "안녕ㅇ하세요" 상황에서 before text length 3글자만큼 지우고 "안녕아" 를 결과적으로 "안녕아하세요" 로 의도대로 정상 입력 됩니다.

> 외부 앱 키보드
> * Swiftkey 키보드
> "안녕하세요" 입력 후 space 띄어쓰기 이후 "안녕" "하세요" 사이로 커서 이동 시 다시 "안녕하세요" 뒤로 커서 자동변경 되는 이슈가 있습니다.
> * Gboard
> 문제가 발생하지 않음 → Gboard 는 reselection 을 지원하지 않아 입력 중인 글자로만 추천어를 만들어 줍니다.
> 사용에 참고 부탁드립니다.
> 감사합니다.



## + 추가 사항
해당 오류에 대한 GoogleIssueTracker 주소 : https://issuetracker.google.com/u/2/issues/195231205
현재 문제가 assign 된 지는 1년이 넘었지만 아직 이렇다할 소식이 없는 상태입니다. ㅠㅠ
