# Validation
스프링 Validation 공부

# Exception(예외)??
사용자의 잘못된 조작이나 개발자의 코딩 실수로 인해 발생하는 프로그램 오류를 뜻한다.
에러와 다른 점은 예외는 예외처리(try-catch)를 통해 프로그램을 종료하지 않고 정상 실행 상태가 유지되도록 할 수 있다.

여기서 자바의 메인 메서드를 실행하면 main이라는 이름의 쓰레드가 실행되는데 예외를 처리하지 못하고 main 메서드를 넘어서면 예외 정보를 남기고 main 쓰레드는 종료되버린다.

# 웹 애플리케이션에서의 예외
![image](https://user-images.githubusercontent.com/97887047/199903735-f6f7db68-3c55-4797-8dde-f9d84b876ceb.png)

애플리케이션 어디선가 예외를 잡지 못한다면 예외는 WAS까지 올라가게되고 해당 예외는 서버 내부에서 처리할 수 없는 예외로 판단하여 HTTP 상태 코드 500(서버 오류)을 반환한다.

# 서블릿 예외처리 및 오류 페이지 등록
서블릿을 통해 예외페이지 처리를 하기 위해서는 여러가지 과정이 필요하다.
>1. WebServerCustomizer를 만든다.
>2. 예외 종류에 따른 ErrorPage 생성하고 추가한다.
>3. 예외 처리용 컨트롤러를 만든다.

![image](https://user-images.githubusercontent.com/97887047/199906229-7dd3ba4d-950a-4899-b075-6b4a42ac9dd1.png)
![image](https://user-images.githubusercontent.com/97887047/199906496-d7c613d6-ea89-43aa-93e8-aa71fe87d1f4.png)


# 스프링 부트에서의 예외 처리 및 오류 페이지 등록
스프링 부트는 위의 과정을 모두 기본으로 제공한다. 즉, ErrorPage를 자동으로 등록하고 /error 라는 경로로 기본 오류 페이지를 설정해준다.
> "BasicErrorController" 라는 컨트롤러가 기본으로 있다.

![image](https://user-images.githubusercontent.com/97887047/199906926-6d031207-2e88-477b-93d6-a233e05d3720.png)

위 사진을 보면 RequestMapping에 경로로 /error가 설정되어있는 것을 볼 수 있다.

결론적으로 개발자가 스프링부트를 사용한다면 예외처리 페이지에 대해 별도로 신경쓸 것이 없다. 
resource/templates 에 error 페키지를 만들어 여기에 각 예외에 맞는 페이지를 생성해 놓는다면 스프링부트가 해당 페이지로 렌더링 해줄 것이다.
>오류 페이지의 우선순위는 자세함이다. 404에러가 발생했을 때 404.html이 있다면 해당 페이지를 연결해줄 것이고 400에러가 났는데 4xx.html이 있다면 400.html이 없기에 4xx.html로 렌더링 해줄 것이다.

# BasicErrorController가 제공하는 기본 정보들 및 설정
> timestamp, status, error, exception, trace, message, errors, path 등의 정보를 제공한다. 

위의 정보들을 모두 사용자에게 보여주는 것은 옳지 못하기에 properties 파일에서 포함 여부를 설정할 수 있다.
- server.error.include-exception : exception 포함 여부
- server.error.include-message : message 포함 여부
- server.error.include-stacktrace : trace 포함 여부
- server.error.include-binding : errors 포함 여부

오류 페이지 경로를 설정할 수도 있다.
> server.error.path=오류페이지경로

# API 예외처리
API는 단순히 예외페이지를 보여주는 것으로는 되지않는다. 이는 예외가 터졌을 때 각 상황에 맞는 오류응답을 정해서 전달해야한다.

## 상태코드 변환
### HandlerExceptionResolver
스프링 MVC는 핸들러 밖으로 예외가 던져졌을 경우 이를 해결하고 동작을 새로 정의할 수 있는 방법을 제공하는데 HandlerExceptionResolver를 사용하면된다.
그러면 예외가 WAS까지 전달되기 이전에 ExceptionResolver에서 가로채서 예외를 해결하고 정상 응답을 할 수 있도록 한다.

ExceptionResolver를 사용하면 예외 상태 코드 변환, 뷰 템플릿 처리, API 응답처리 등을 할 수 있게 된다.

>ExceptionResolver를 작성하면 이를 등록해야 사용할 수 있다.
>1. Config 클래스 파일에 WebMvcConfigurer를 상속받는다.
>2. extendHandlerExceptionResovlers 메서드에 작성한 핸들러를 추가한다.

![image](https://user-images.githubusercontent.com/97887047/199920994-b1a8f3c0-75c5-4eb3-a255-06a6ddc0eedb.png)

### ResponseStatusExceptionResolver
예외에 따라 HTTP 상태코드를 지정해주는 역할을 한다.
커스텀 예외에서는 @ResponseStatus 어노테이션을 통해 상태코드 및 메시지를 설정할 수 있다. 하지만 시스템에서 정해준 예외 또는 라이브러리 예외같은 경우에는 사용자가 따로 코드를 변환할 수 없다. 그래서 나온 것이 ResponseStatusException이다.

![image](https://user-images.githubusercontent.com/97887047/199924636-4c0d5ce7-5544-4512-8601-8fe79f725ad1.png)

위 사진 처럼 상태코드, 메시지, 예외까지 설정할 수 있다.

## @ExceptionHandler
API 예외처리를 위해 스프링에서 제공하는 어노테이션이다.
@ExceptionHandler 어노테이션을 컨트롤러 내부에 선언하고, 처리하고 싶은 예외를 지정해주면 된다. 해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출되면서 예외가 잡힌다.
이 어노테이션을 사용하면 서블릿 컨테이너까지 올라가지 않고 딱 끝나버린다. 정상흐름으로 끝이난 것이다.

![image](https://user-images.githubusercontent.com/97887047/199928900-415a9de7-1b35-426d-b794-ca40eafb57cc.png)

위에 사진을보면 @ResponseStatus를 통해 응답코드를 따로 지정해주었다. 이는 @ExceptionHandler가 반환을 할 때 정상흐름으로 반환하여 HTTP 상태코드를 200으로 반환하기 때문이다. 

![image](https://user-images.githubusercontent.com/97887047/199929308-fe878771-72ed-4f60-93fb-7bd8bd19998d.png)

오류가 났는데 200은 이상하다. 따라서 상태코드를 따로 지정하여 전달하였다.
다른 방법으로는 ResponseEntity<>를 반환타입으로 지정하여 body와 함께 Http Status를 지정하여 반환할 수도 있다.

> 참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다.

컨트롤러에 예외처리를 모두 넣는다면 코드가 길어지고 보기 좋지 않을 것이다. 따라서 @ControllerAdvice 또는 @RestControllerAdvice 어노테이션을 이용하여 예외를 따로 분리할 수 있다. (이 둘은 @Controller와 @RestController의 차의와 거의 동일하다)

![image](https://user-images.githubusercontent.com/97887047/199934483-f5a765cb-9705-4045-92fb-cc5f2b77738e.png)

대상을 따로 지정하지 않는다면 모든 컨트롤러에 대해 적용된다.
