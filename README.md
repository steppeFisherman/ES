# ES
_Описание:_

Клиент-серверное приложение для контроля текущей геопозиции. Клиенту необходимо раз в сутки отправить геолокацию, в случае если данное событие не произошло
оператор колл-центра связывается с клиентом. Клиент имеет возможность послать сигнал тревоги с текущей геопозицией, 
оператор при этом реагирует по  определенному алгоритму действий. Также клиент имеет возможность позвонить в колл-центр(звонилка) и посмотреть историю событий в зависимости от выбранной даты. 
На стороне оператора установлено другое приложение с базой данных всех клиентов для онлайн контроля всех текущих событий всех клиентов.

___

_Стэк:_

__Firebase, Room, Hilt, Navigation Component, MVVM, LiveData, Kotlin Coroutines, ViewBinding, Clean Architecture__     
___

![Screenshot_permission](https://user-images.githubusercontent.com/26350957/211198112-0d5b3d03-2b8c-468a-a0c2-0acf6146a058.png)
![Screenshot_phone_enter](https://user-images.githubusercontent.com/26350957/211198096-6c2d984d-c8bc-4a53-8fe7-316210ef3494.png)
![Screenshot_main png](https://user-images.githubusercontent.com/26350957/211198147-027307ef-82a9-4e54-ad45-08f070eba02e.jpg)
![Screenshot_panic_pressed](https://user-images.githubusercontent.com/26350957/211198172-63f4b1bb-deac-4902-bbec-6569c421d363.png)
![Screenshot_history](https://user-images.githubusercontent.com/26350957/211198153-cb5a36ba-22df-4044-b02c-5ecc7d0e0c12.png)
![Screenshot_map](https://user-images.githubusercontent.com/26350957/211198205-cda9bb4b-49d6-4c40-8060-68f326cc5d1d.png)
![Screenshot_profile](https://user-images.githubusercontent.com/26350957/211198240-58294c42-690c-4a64-890b-5bf8ce60f001.png)
![Screenshot_calendar](https://user-images.githubusercontent.com/26350957/211198246-dfd441f1-3b75-44bf-9bf8-12b7f84007f2.png)
![Screenshot_call](https://user-images.githubusercontent.com/26350957/211198257-60568a14-86a8-4269-a656-d2dc9f3409f2.png)
__
           
