package com.lostedin.ecosystem.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    //TODO: переписать логику контроллеров (Через DTO не должно передаваться user_id)


    /*TODO: (Идея для будущего update)
        Сделать Профиль для developers. При логин через OAuth показать логотип приложения
        к которому пользователь хочет зайти.
     */

    /*TODO: (Идея для будущего update)
        Вынести Developers в отдельный микросервис если фича будет расширятся
     */

    /*TODO: (Идея для будущего update)
        Логировать ошибки и клиентам вернуть только краткое, неполное ошибки без stacktrace
     */

    /*TODO: (Идея в целом для экосистемы)
        Написать микросервис который будет управлять связанные аккаунты.
        Например: сервис который удалит данные пользователей если удален главный аккаунт или наоборот
        уведомить главный user Service если удален учетный запись в других сервисах.
        PS: реализовать с помощью Kafka
     */

}
