insert into telegram_message(name, text)
values ('ADD_SERVICE', 'In order to start getting messages, choose services you want to get messages about. Enter /addservice where all services will be printed, to add one you need to press button with the name of the service. To stop selecting press button STOP or if you call another method selecting process will be automatically stopped.');
insert into telegram_message(name, text)
values ('REMOVE_SERVICE', 'If you want to remove some services you have previously subscribed please enter /removeservice. The list of active services will be appeared. To remove a service press button with service name. To stop removing press button STOP or if you call another method removing process will be automatically stopped.');
insert into telegram_message(name, text)
values ('TWO_DATES_TOP_N', 'In order to get top N failed URL messages between two dates enter /gettop and after you need to enter two dates separately in format (dd-MM-yyyy). For example, you firstly enter 22-02-2020 and then you enter 22-04-2020.');
insert into telegram_message(name, text)
values ('TWO_DATES_ALL', 'In order to get all failed URL messages between two dates enter /getall and after you need to enter two dates separately in format (dd-MM-yyyy). For example, you firstly enter 22-02-2020 and then you enter 22-04-2020.');
insert into telegram_message(name, text)
values ('GET_REPORT', 'In order to get all failed URL messages between two dates in excel file enter /getreport and after you need to enter two dates separately in format (dd-MM-yyyy). For example, you firstly enter 22-02-2020 and then you enter 22-04-2020.');