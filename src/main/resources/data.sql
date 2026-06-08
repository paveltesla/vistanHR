-- 1. НАПОЛНЕНИЕ ТАБЛИЦЫ ВАКАНСИЙ (ЯВНО УКАЗЫВАЕМ ID)
INSERT INTO vacancies (id, title, department, vacancy_type, description, requirements, responsibilities, salary_min,
                       salary_max, required_experience, work_schedule, employment_type, location, created_date, status,
                       applications_count)
SELECT *
FROM (SELECT 1 AS id, -- Фиксируем ID = 1
             'Токарь-карусельщик (5-6 разряд)' AS title,
             'Механический цех' AS department,
             'WORKER' AS vacancy_type,
             'Обработка деталей на токарно-карусельных станках для станкостроительного производства.' AS description,
             'Наличие 5-6 разряда, чтение сложных технологических чертежей, опыт от 2 лет.' AS requirements,
             'Токарная обработка узлов, настройка станков, контроль геометрии деталей.' AS responsibilities,
             1800 AS salary_min,
             2600 AS salary_max,
             2 AS required_experience,
             'SHIFT' AS work_schedule,
             'CONTRACT' AS employment_type,
             'Витебск, ул. Димитрова, 36' AS location,
             CURRENT_TIMESTAMP AS created_date,
             'OPEN' AS status,
             2 AS applications_count
      UNION ALL
      SELECT 2, -- Фиксируем ID = 2
             'Инженер-конструктор',
             'Конструкторско-технологический отдел',
             'EMPLOYEE',
             'Проектирование узлов металлорежущих и зубообрабатывающих станков.',
             'Высшее техническое образование (машиностроение), знание Компас-3D / SolidWorks.',
             'Разработка конструкторской документации, участие в испытаниях узлов.',
             2000,
             3200,
             3,
             'FULL_TIME',
             'PERMANENT',
             'Витебск, ул. Димитрова, 36',
             CURRENT_TIMESTAMP,
             'OPEN',
             1
      UNION ALL
      SELECT 3, -- Фиксируем ID = 3
             'Фрезеровщик',
             'Механический цех',
             'WORKER',
             'Фрезерная обработка корпусных деталей станков.',
             'Опыт работы на вертикально- и горизонтально-фрезерных станках, от 3 разряда.',
             'Фрезерование плоскостей, пазов, пазование валов.',
             1600,
             2200,
             1,
             'SHIFT',
             'CONTRACT',
             'Витебск, ул. Димитрова, 36',
             CURRENT_TIMESTAMP,
             'OPEN',
             1
      UNION ALL
      SELECT 4, -- Фиксируем ID = 4
             'Контролер ОТК (станочные и слесарные работы)',
             'Отдел технического контроля',
             'EMPLOYEE',
             'Проверка качества выпускаемых деталей и узлов станков.',
             'Умение пользоваться мерительным инструментом (микрометры, нутромеры), чтение чертежей.',
             'Пооперационный контроль деталей, ведение журналов брака.',
             1500,
             1900,
             1,
             'FULL_TIME',
             'PERMANENT',
             'Витебск, ул. Димитрова, 36',
             CURRENT_TIMESTAMP,
             'OPEN',
             1
      UNION ALL
      SELECT 5, -- Фиксируем ID = 5
             'Электромонтер по ремонту оборудования',
             'Служба главного инженера',
             'WORKER',
             'Обслуживание и ремонт электрооборудования станков и цеховых сетей.',
             '4 группа по электробезопасности, чтение принципиальных электрических схем.',
             'Ремонт релейно-контакторных схем, обслуживание автоматики станков.',
             1700,
             2400,
             2,
             'SHIFT',
             'CONTRACT',
             'Витебск, ул. Димитрова, 36',
             CURRENT_TIMESTAMP,
             'OPEN',
             0) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM vacancies);


-- 2. НАПОЛНЕНИЕ ТАБЛИЦЫ ОТКЛИКОВ
INSERT INTO job_applications (vacancy_id, full_name, email, phone, cover_letter, resume, experience, skills,
                              expected_salary, application_date, application_status, review_notes, position) -- <- Добавили сюда
SELECT *
FROM (SELECT 1 AS vacancy_id,
             'Ковалев Николай Петрович' AS full_name,
             'kovalev_tokar@mail.ru' AS email,
             '+375291234567' AS phone,
             'Ищу постоянную работу, готов к двухсменному графику.' AS cover_letter,
             'Работал на Витебском заводе заточников 4 года. Станки 1512, 1516. Разряд 5-й подтвержден книжкой.' AS resume,
             4 AS experience,
             'Чтение чертежей, заточка инструмента, работа с индикаторными нутромерами' AS skills,
             2000 AS expected_salary,
             CURRENT_TIMESTAMP AS application_date,
             'PENDING' AS application_status,
             '' AS review_notes,
             'Токарь' AS position -- <- Указали позицию
      UNION ALL
      SELECT 1,
          'Иванов Дмитрий Александрович',
          'ivanov_d@gmail.com',
          '+375336543210',
          'Слышал о вашем заводе, хочу перевестись.',
          'Опыт работы токарем 6 лет. Работал в Минске, вернулся в Витебск. Все разряды в наличии.',
          6,
          'Работа на карусельных станках, обработка крупногабаритных литых заготовок',
          2500,
          CURRENT_TIMESTAMP,
          'PENDING',
          '',
          'Токарь-карусельщик' -- <- Указали позицию
      UNION ALL
      SELECT 2,
          'Радкевич Ольга Владимировна',
          'radkevich_design@tut.by',
          '+375298889911',
          'Высылаю резюме на должность инженера-конструктора.',
          'Окончила ВГТУ по специальности "Технология машиностроения". 3 года проектировала штампы и пресс-формы.',
          3,
          'SolidWorks, Компас-3D, AutoCad, расчет на прочность (ANSYS)',
          2200,
          CURRENT_TIMESTAMP,
          'REVIEWED',
          'Хорошее резюме, профильное образование. Пригласить на техническое интервью.',
          'Инженер-конструктор' -- <- Указали позицию
      UNION ALL
      SELECT 3,
          'Петров Игорь Николаевич',
          'petrov_frez@mail.ru',
          '+375257776655',
          'Фрезеровщик 4 разряда.',
          'Опыт работы на производстве 2 года. Обработка сталей, чугуна. Дисциплинирован.',
          2,
          'Вертикально-фрезерные станки, работа по упорам и разметке',
          1700,
          CURRENT_TIMESTAMP,
          'PENDING',
          '',
          'Фрезеровщик' -- <- Указали позицию
      UNION ALL
      SELECT 4,
          'Сидоренко Анна Ивановна',
          'sidorenko_otk@yandex.ru',
          '+375293334455',
          'Резюме во вложении.',
          'Опыт работы контролером ОТК в машиностроении 5 лет. Знание стандартов ГОСТ и СТБ.',
          5,
          'Инструментальный контроль качества, оформление актов брака, чтение КД',
          1600,
          CURRENT_TIMESTAMP,
          'INTERVIEW',
          'Собеседование назначено на четверг 14:00',
          NULL) AS tmp -- <- Если позиции нет, пишем NULL
WHERE NOT EXISTS (SELECT 1 FROM job_applications);
-- Говорим базе данных, что следующие ID нужно начинать с 6
ALTER TABLE vacancies ALTER COLUMN id RESTART WITH 6;