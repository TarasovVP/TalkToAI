package com.vnstudio.talktoai.domain.resources

import androidx.compose.ui.text.intl.Locale
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_RU
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_UK

object StringResourcesEN {
    const val APP_NAME = "Talk to AI"
    const val APP_ID = "ca-app-pub-8778007212154570~5099361664"
    // App
    const val APP_EXIT = "Do you want to exit the app?"
    const val APP_NETWORK_UNAVAILABLE_REPEAT = "No internet connection.\nTo continue, connect to the internet and try again."
    // Buttons
    const val BUTTON_NEXT = "Next"
    const val BUTTON_ACCEPT = "Allow"
    const val BUTTON_CANCEL = "Cancel"
    const val BUTTON_OK = "OK"
    const val BUTTON_CLOSE = "Close"
    // Onboarding
    const val ONBOARDING_INTRO = "Hello! I am Smart Blocker. I will be your assistant in blocking unwanted calls and my smart filters will help me with this."
    const val ONBOARDING_FILTER_CONDITIONS = "Smart filters will analyze incoming calls based on criteria you specify and block or permit them."
    const val ONBOARDING_INFO = "I'm always available to help you understand how I work. To do this, find the icon <img src =\'ic_info\'> and follow it to the screen with the instruction."
    const val ONBOARDING_PERMISSIONS = "The phone will ask for the necessary permissions to work properly. To do this, select “Allow” in the system windows."
    // Authorization
    const val AUTHORIZATION_ENTRANCE = "Entrance"
    const val AUTHORIZATION_WITH_GOOGLE_ACCOUNT = "With Google"
    const val AUTHORIZATION_ENTER = "Login"
    const val AUTHORIZATION_LOGIN_WITH_GOOGLE_DESCRIPTION = "Login with Google"
    const val AUTHORIZATION_OR = "Or"
    const val AUTHORIZATION_OR_WITH_EMAIL_PASSWORD_DESCRIPTION = "Or login with Email and password"
    const val AUTHORIZATION_OR_WITHOUT_ACCOUNT_DESCRIPTION = "Or login without authorisation"
    const val AUTHORIZATION_EMAIL = "Email"
    const val AUTHORIZATION_PASSWORD = "Password"
    const val AUTHORIZATION_SIGN_UP = "Sign up"
    const val AUTHORIZATION_SIGN_UP_WITH_GOOGLE_DESCRIPTION = "Sign up with Google"
    const val AUTHORIZATION_FORGOT_PASSWORD = "Forgot password?"
    const val AUTHORIZATION_CONTINUE_WITHOUT_ACCOUNT = "Without authorisation"
    const val AUTHORIZATION_FORGOT_PASSWORD_TITLE = "Enter your email, and we will send you instructions."
    const val AUTHORIZATION_ENTER_EMAIL = "Enter your Email"
    const val AUTHORIZATION_PASSWORD_RESET_SUCCESS = "An email has been sent to your Email. Check in your inbox."
    const val AUTHORIZATION_SIGN_UP_TITLE = "Haven`t got an account?&#160;"
    const val AUTHORIZATION_ENTRANCE_TITLE = "Got an account?&#160;"
    const val AUTHORIZATION_SIGNING_UP = "Sign Up"
    const val AUTHORIZATION_ACCOUNT_NOT_EXIST = "There is no user with this email address. First you need to create an account. Go to the registration screen?"
    const val AUTHORIZATION_ACCOUNT_EXIST = "A user with this email already exists. You need to log in. Go to the login screen?"
    const val AUTHORIZATION_UNAUTHORIZED_ENTER = "An unauthorized user has data stored locally within a single session. Enter anyway?"
    const val AUTHORIZATION_UNAUTHORIZED_ENTER_BUTTON = "Enter"
    const val AUTHORIZATION_NETWORK_UNAVAILABLE = "No internet connection.\nTo continue, connect to the internet and log in again."
    // Empty state
    const val EMPTY_STATE_BLOCKERS = "Blocker list is empty"
    // Settings screens
    const val SETTINGS = "Settings"
    const val SETTINGS_CHAT = "Chat settings"
    const val SETTINGS_ACCOUNT = "Your account"
    const val SETTINGS_LANGUAGE = "Language"
    const val SETTINGS_THEME = "Selecting a theme"
    const val SETTINGS_FEEDBACK = "Contact developer"
    const val SETTINGS_PRIVACY_POLICY = "Privacy Policy"
    // Settings chat

    // Settings account
    const val SETTINGS_ACCOUNT_LOG_OUT_TITLE = "Logout"
    const val SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT_TITLE = "Leave"
    const val SETTINGS_ACCOUNT_UNAUTHORISED = "Unauthorised"
    const val SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE = "Change password"
    const val SETTINGS_ACCOUNT_DELETE_TITLE = "Delete account"
    const val SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD = "Current password"
    const val SETTINGS_ACCOUNT_ENTER_NEW_PASSWORD = "New password"
    const val SETTINGS_ACCOUNT_CHANGE_PASSWORD_SUCCEED = "Password changed successfully"
    const val SETTINGS_ACCOUNT_LOG_OUT = "Are you sure you want to log out?"
    const val SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT = "If you log out of an unauthorized account, you will lose all created data. Sign up to save. Still leave?"
    const val SETTINGS_ACCOUNT_EMAIL_DELETE = "The account and all created data will be permanently deleted. Enter your current password to continue."
    const val SETTINGS_ACCOUNT_GOOGLE_DELETE = "The account and all created data will be permanently deleted. Google may ask you to confirm your authorization to continue."
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TITLE = "Transfer data?"
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_ON = "The created data will be transferred to your account."
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_OFF = "The created data will be deleted."
    const val SETTINGS_ACCOUNT_EXIST = "A user with this email already exists. You can go to this account. If the choice of data transfer is enabled, they will be transferred to the account, if it is disabled, they will be permanently deleted. Go?"
    const val EMPTY_STATE_ACCOUNT = "Your data is stored locally within a single session and will be lost if you log out of this account, clear the cache, or reinstall the application. To connect remote data storage and access it from any device, register."
    // Settings language
    const val SETTINGS_LANGUAGE_RUSSIAN = "Russian"
    const val SETTINGS_LANGUAGE_UKRAINIAN = "Ukrainian"
    const val SETTINGS_LANGUAGE_ENGLISH = "English"
    // Settings theme
    const val  SETTINGS_THEME_DAY = "Light"
    const val  SETTINGS_THEME_NIGHT = "Dark"
    const val  SETTINGS_THEME_AUTO = "According to phone settings"
    // Settings feedback
    const val  SETTINGS_FEEDBACK_TITLE = "If you have any suggestions or comments, please describe them below"
    const val  SETTINGS_FEEDBACK_HINT = "Message"
    const val  SETTINGS_FEEDBACK_SEND_BUTTON = "Submit"
    const val  SETTINGS_FEEDBACK_SEND_SUCCESS = "Message sent successfully!"
    // Settings privacy
    const val PRIVACY_POLICY = "<![CDATA[<html><head><meta charset=\\'utf–8\\'><meta name=\\'viewport\\' content=\\'width=device–width\\'><title>Privacy Policy</title><style>body {font–family: \\'Helvetica Neue\\', Helvetica, Arial, sans–serif;padding: 1em;}</style></head><body><strong>Privacy Policy</strong><p> Tarasov Volodymyr built the SmartBlocker app as a Commercial app. This SERVICE is provided by Tarasov Volodymyr and is intended for use as is. </p><p> This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service. </p><p> If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy. </p><p> The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at SmartBlocker unless otherwise defined in this Privacy Policy. </p><p><strong>Information Collection and Use</strong></p><p> For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way. </p><div><p> The app does use third–party services that may collect information used to identify you. </p><p> Link to the privacy policy of third–party service providers used by the app </p><ul><li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Google Play Services</a></li><li><a href=\"AdMob policies and restrictions – Google AdMob Help target=\" _blank\" rel=\"noopener noreferrer\">AdMob</a></li><li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\" rel=\"noopener noreferrer\">Google Analytics for Firebase</a> </li><li><a href=\"Firebase Support target=\" _blank\" rel=\"noopener noreferrer\">Firebase Crashlytics</a></li></ul></div><p><strong>Log Data</strong></p><p> I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third–party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics. </p><p><strong>Cookies</strong></p><p> Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device\\'s internal memory. </p><p> This Service does not use these “cookies” explicitly. However, the app may use third\\–party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service. </p><p><strong>Service Providers</strong></p><p> I may employ third\\–party companies and individuals due to the following reasons: </p><ul><li>To facilitate our Service;</li><li>To provide the Service on our behalf;</li><li>To perform Service–related services; or</li><li>To assist us in analyzing how our Service is used.</li></ul><p> I want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose. </p><p><strong>Security</strong></p><p> I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security. </p><p><strong>Links to Other Sites</strong></p><p> This Service may contain links to other sites. If you click on a third–party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third–party sites or services. </p><p><strong>Children\\’s Privacy</strong></p><div><p> These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do the necessary actions. </p></div><p><strong>Changes to This Privacy Policy</strong></p><p> I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page. </p><p>This policy is effective as of 2023–01–04</p><p><strong>Contact Us</strong></p><p> If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at blockersmart2022@gmail.com. </p></body></html>]]>"
    // Menu item
    const val  MENU_INFO = "Info"
    const val  MENU_DELETE = "Delete"
    const val  MENU_SEARCH = "Search"
    const val  ERROR_MESSAGE = "An error occurred"
}

object StringResourcesRU {
    const val APP_NAME = "Talk to AI"
    const val APP_ID = "ca-app-pub-8778007212154570~5099361664"
    // App
    const val APP_EXIT = "Вы хотите выйти из приложения?"
    const val APP_NETWORK_UNAVAILABLE_REPEAT = "Интернет соединение отсутствует.\nДля продолжения, подключите интернет и повторите действие."
    // Buttons
    const val BUTTON_NEXT = "Дальше"
    const val BUTTON_ACCEPT = "Разрешить"
    const val BUTTON_CANCEL = "Отмена"
    const val BUTTON_OK = "OK"
    const val BUTTON_CLOSE = "Закрыть"
    // Onboarding
    const val ONBOARDING_INTRO = "Привет! Я - Smart Blocker. Я буду Вашим помощником в блокировании нежелательных звонков и в этом мне помогут мои умные фильтры."
    const val ONBOARDING_FILTER_CONDITIONS = "Умные фильтры будут анализировать входящие вызовы по заданным Вами признакам и блокировать или разрешать их."
    const val ONBOARDING_INFO = "Я всегда готов помочь разобраться в том, как я работаю. Для этого найдите иконку <img src ='ic_info'> и по ней перейдите на экран с инструкцией."
    const val ONBOARDING_PERMISSIONS = "Для корректной работы телефон запросит необходимые разрешения. Для этого выберите “Разрешить” в системных окнах."
    // Authorization
    const val AUTHORIZATION_ENTRANCE = "Вход"
    const val AUTHORIZATION_WITH_GOOGLE_ACCOUNT = "С помощью Google"
    const val AUTHORIZATION_ENTER = "Войти"
    const val AUTHORIZATION_LOGIN_WITH_GOOGLE_DESCRIPTION = "Войти с помощью Google"
    const val AUTHORIZATION_OR = "Или"
    const val AUTHORIZATION_OR_WITH_EMAIL_PASSWORD_DESCRIPTION = "Или с Email и паролем"
    const val AUTHORIZATION_OR_WITHOUT_ACCOUNT_DESCRIPTION = "Или войти без авторизации"
    const val AUTHORIZATION_EMAIL = "Email"
    const val AUTHORIZATION_PASSWORD = "Пароль"
    const val AUTHORIZATION_SIGN_UP = "Регистрация"
    const val AUTHORIZATION_SIGN_UP_WITH_GOOGLE_DESCRIPTION = "Регистрация с помощью Google"
    const val AUTHORIZATION_FORGOT_PASSWORD = "Забыли пароль?"
    const val AUTHORIZATION_CONTINUE_WITHOUT_ACCOUNT = "Без авторизации"
    const val AUTHORIZATION_FORGOT_PASSWORD_TITLE = "Введите ваш Email и мы отправим Вам инструкции."
    const val AUTHORIZATION_ENTER_EMAIL = "Введите ваш Email"
    const val AUTHORIZATION_PASSWORD_RESET_SUCCESS = "Письмо отправлено на ваш Email. Проверьте во входящих."
    const val AUTHORIZATION_SIGN_UP_TITLE = "Еще нет аккаунта?&#160;"
    const val AUTHORIZATION_ENTRANCE_TITLE = "Есть аккаунт?&#160;"
    const val AUTHORIZATION_SIGNING_UP = "Зарегистрироваться"
    const val AUTHORIZATION_ACCOUNT_NOT_EXIST = "Пользователя с таким Email не существует. Сначала необходимо создать аккаунт. Перейти на экран регистрации?"
    const val AUTHORIZATION_ACCOUNT_EXIST = "Пользователь с таким Email уже существует. Необходимо авторизоваться. Перейти на экран логина?"
    const val AUTHORIZATION_UNAUTHORIZED_ENTER = "У неавторизованного пользователя данные хранятся локально в рамках одной сессии. Все равно войти?"
    const val AUTHORIZATION_UNAUTHORIZED_ENTER_BUTTON = "Войти"
    const val AUTHORIZATION_NETWORK_UNAVAILABLE = "Интернет соединение отсутствует.\nДля продолжения, подключите интернет и совершите повторный вход."
    // Empty state
    const val EMPTY_STATE_BLOCKERS = "Список блокировщиков пуст"
    // Settings screens
    const val SETTINGS = "Настройки"
    const val SETTINGS_CHAT = "Настройки чата"
    const val SETTINGS_ACCOUNT = "Ваш аккаунт"
    const val SETTINGS_LANGUAGE = "Язык"
    const val SETTINGS_THEME = "Выбор темы"
    const val SETTINGS_FEEDBACK = "Написать разработчику"
    const val SETTINGS_PRIVACY_POLICY = "Политика конфиденциальности"
    // Settings chat

    // Settings account
    const val SETTINGS_ACCOUNT_LOG_OUT_TITLE = "Выйти"
    const val SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT_TITLE = "Выйти"
    const val SETTINGS_ACCOUNT_UNAUTHORISED = "Неавторизованный"
    const val SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE = "Сменить пароль"
    const val SETTINGS_ACCOUNT_DELETE_TITLE = "Удалить аккаунт"
    const val SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD = "Текущий пароль"
    const val SETTINGS_ACCOUNT_ENTER_NEW_PASSWORD = "Новый пароль"
    const val SETTINGS_ACCOUNT_CHANGE_PASSWORD_SUCCEED = "Пароль успешно изменен"
    const val SETTINGS_ACCOUNT_LOG_OUT = "Вы действительно хотите разлогиниться?"
    const val SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT = "При выходе из неавторизованого аккаунта вы потеряете все созданные данные. Для сохранения зарегистрируйтесь. Все равно выйти?"
    const val SETTINGS_ACCOUNT_EMAIL_DELETE = "Аккаунт и все созданные данные будут удалены безвозвратно. Для продолжения введите текущий пароль."
    const val SETTINGS_ACCOUNT_GOOGLE_DELETE = "Аккаунт и все созданные данные будут удалены безвозвратно. Для продолжения Google может запросить подтверждение авторизации."
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TITLE = "Перенести данные?"
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_ON = "Cозданные данные будут перенесены в ваш аккаунт."
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_OFF = "Cозданные данные будут удалены."
    const val SETTINGS_ACCOUNT_EXIST = "Пользователь с таким Email уже существует. Вы можете перейти в этот аккаунт. При включенном выборе переноса данных - они будут перенесены в аккаунт, при выключенном - удалены безвозвартно. Перейти?"
    const val EMPTY_STATE_ACCOUNT = "Ваши данные хранятся локально в рамках одной сессии и будут потеряны при выходе из этого аккаунта, очистке кеша или переустановке приложения. Для подключения удаленного хранения данных и доступа к нему с любого устройства, зарегистрируйтесь."
    // Settings language
    const val SETTINGS_LANGUAGE_RUSSIAN = "Русский"
    const val SETTINGS_LANGUAGE_UKRAINIAN = "Украинский"
    const val SETTINGS_LANGUAGE_ENGLISH = "Английский"
    // Settings theme
    const val SETTINGS_THEME_DAY = "Светлая"
    const val SETTINGS_THEME_NIGHT = "Темная"
    const val SETTINGS_THEME_AUTO = "Согласно настройек телефона"
    // Settings feedback
    const val SETTINGS_FEEDBACK_TITLE = "Если у вас есть предложения или замечания вы можете их описать ниже"
    const val SETTINGS_FEEDBACK_HINT = "Сообщение"
    const val SETTINGS_FEEDBACK_SEND_BUTTON = "Отправить"
    const val SETTINGS_FEEDBACK_SEND_SUCCESS = "Сообщение успешно отправлено!"
    // Settings privacy
    const val PRIVACY_POLICY = "<![CDATA[<html><head><meta charset=\\'utf–8\\'><meta name=\\'viewport\\' content=\\'width=device–width\\'><title>Privacy Policy</title><style>body {font–family: \\'Helvetica Neue\\', Helvetica, Arial, sans–serif;padding: 1em;}</style></head><body><strong>Privacy Policy</strong><p> Tarasov Volodymyr built the SmartBlocker app as a Commercial app. This SERVICE is provided by Tarasov Volodymyr and is intended for use as is. </p><p> This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service. </p><p> If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy. </p><p> The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at SmartBlocker unless otherwise defined in this Privacy Policy. </p><p><strong>Information Collection and Use</strong></p><p> For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way. </p><div><p> The app does use third–party services that may collect information used to identify you. </p><p> Link to the privacy policy of third–party service providers used by the app </p><ul><li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Google Play Services</a></li><li><a href=\"AdMob policies and restrictions – Google AdMob Help target=\" _blank\" rel=\"noopener noreferrer\">AdMob</a></li><li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\" rel=\"noopener noreferrer\">Google Analytics for Firebase</a> </li><li><a href=\"Firebase Support target=\" _blank\" rel=\"noopener noreferrer\">Firebase Crashlytics</a></li></ul></div><p><strong>Log Data</strong></p><p> I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third–party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics. </p><p><strong>Cookies</strong></p><p> Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device\\'s internal memory. </p><p> This Service does not use these “cookies” explicitly. However, the app may use third\\–party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service. </p><p><strong>Service Providers</strong></p><p> I may employ third\\–party companies and individuals due to the following reasons: </p><ul><li>To facilitate our Service;</li><li>To provide the Service on our behalf;</li><li>To perform Service–related services; or</li><li>To assist us in analyzing how our Service is used.</li></ul><p> I want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose. </p><p><strong>Security</strong></p><p> I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security. </p><p><strong>Links to Other Sites</strong></p><p> This Service may contain links to other sites. If you click on a third–party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third–party sites or services. </p><p><strong>Children\\’s Privacy</strong></p><div><p> These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do the necessary actions. </p></div><p><strong>Changes to This Privacy Policy</strong></p><p> I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page. </p><p>This policy is effective as of 2023–01–04</p><p><strong>Contact Us</strong></p><p> If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at blockersmart2022@gmail.com. </p></body></html>]]>"
    // Menu item
    const val MENU_INFO = "Инфо"
    const val MENU_DELETE = "Удалить"
    const val MENU_SEARCH = "Поиск"
    const val ERROR_MESSAGE = "Произошла ошибка"
}

object StringResourcesUK {
    // App
    const val APP_EXIT = "Ви хочете вийти з програми?"
    const val APP_NETWORK_UNAVAILABLE_REPEAT = "Інтернет з'єднання відсутнє.\nДля продовження, підключіть інтернет і повторіть дію."
    // Buttons
    const val BUTTON_NEXT = "Далі"
    const val BUTTON_ACCEPT = "Дозволити"
    const val BUTTON_CANCEL = "Скасувати"
    const val BUTTON_OK = "OK"
    const val BUTTON_CLOSE = "Закрити"
    // Onboarding
    const val ONBOARDING_INTRO = "Привіт! Я – Smart Blocker. Я буду вашим помічником у блокуванні небажаних дзвінків і в цьому мені допоможуть мої розумні фільтри."
    const val ONBOARDING_FILTER_CONDITIONS = "Розумні фільтри будуть аналізувати вхідні дзвінки за заданими Вами ознаками та блокувати або дозволяти їх."
    const val ONBOARDING_INFO = "Я завжди готовий допомогти розібратися в тому, як я працюю. Для цього знайдіть іконку <img src ='ic_info'> і по ній перейдіть на екран з інструкцією."
    const val ONBOARDING_PERMISSIONS = "Для коректної роботи телефон запросить необхідні дозволи. Для цього виберіть \"Дозволити\" у системних вікнах."
    // Authorization
    const val AUTHORIZATION_ENTRANCE = "Вхід"
    const val AUTHORIZATION_WITH_GOOGLE_ACCOUNT = "За допомогою Google"
    const val AUTHORIZATION_ENTER = "Увійти"
    const val AUTHORIZATION_OR = "Або"
    const val AUTHORIZATION_LOGIN_WITH_GOOGLE_DESCRIPTION = "Або увійти за допомогою Google"
    const val AUTHORIZATION_OR_WITH_EMAIL_PASSWORD_DESCRIPTION = "Або з Email та паролем"
    const val AUTHORIZATION_OR_WITHOUT_ACCOUNT_DESCRIPTION = "Або увійти без авторизації"
    const val AUTHORIZATION_EMAIL = "Email"
    const val AUTHORIZATION_PASSWORD = "Пароль"
    const val AUTHORIZATION_SIGN_UP = "Реєстрація"
    const val AUTHORIZATION_SIGN_UP_WITH_GOOGLE_DESCRIPTION = "Реєстрація за допомогою Google"
    const val AUTHORIZATION_FORGOT_PASSWORD = "Забули пароль?"
    const val AUTHORIZATION_CONTINUE_WITHOUT_ACCOUNT = "Без авторизації"
    const val AUTHORIZATION_FORGOT_PASSWORD_TITLE = "Введіть ваш Email і ми відправимо Вам інструкції."
    const val AUTHORIZATION_ENTER_EMAIL = "Введіть ваш Email"
    const val AUTHORIZATION_PASSWORD_RESET_SUCCESS = "Лист надіслано на ваш Email. Перевірте у вхідних."
    const val AUTHORIZATION_SIGN_UP_TITLE = "Ще немає облікового запису?&#160;"
    const val AUTHORIZATION_ENTRANCE_TITLE = "Є обліковий запис?&#160;"
    const val AUTHORIZATION_SIGNING_UP = "Зареєструватися"
    const val AUTHORIZATION_ACCOUNT_NOT_EXIST = "Користувача з електронною поштою не існує. Спочатку необхідно створити обліковий запис. Перейти на екран реєстрації?"
    const val AUTHORIZATION_ACCOUNT_EXIST = "Користувач із таким Email вже існує. Потрібно авторизуватися. Перейти на екран логіну?"
    const val AUTHORIZATION_UNAUTHORIZED_ENTER = "У неавторизованого користувача дані зберігаються локально у межах однієї сесії. Все одно увійти?"
    const val AUTHORIZATION_UNAUTHORIZED_ENTER_BUTTON = "Увійти"
    const val AUTHORIZATION_NETWORK_UNAVAILABLE = "Інтернет з'єднання відсутнє.\nДля продовження, підключіть інтернет і здійсніть повторний вхід."
    // Empty state
    const val EMPTY_STATE_BLOCKERS = "Список блокувальників порожній"
    // Settings screens
    const val SETTINGS = "Налаштування"
    const val SETTINGS_CHAT = "Налаштування чату"
    const val SETTINGS_ACCOUNT = "Ваш обліковий запис"
    const val SETTINGS_LANGUAGE = "Мова"
    const val SETTINGS_THEME = "Вибір теми"
    const val SETTINGS_FEEDBACK = "Написати розробнику"
    const val SETTINGS_PRIVACY_POLICY = "Політика конфіденційності"
    // Settings account
    const val SETTINGS_ACCOUNT_LOG_OUT_TITLE = "Вийти"
    const val SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT_TITLE = "Вийти"
    const val SETTINGS_ACCOUNT_UNAUTHORISED = "Неавторизований"
    const val SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE = "Змінити пароль"
    const val SETTINGS_ACCOUNT_DELETE_TITLE = "Видалити обліковий запис"
    const val SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD = "Поточний пароль"
    const val SETTINGS_ACCOUNT_ENTER_NEW_PASSWORD = "Новий пароль"
    const val SETTINGS_ACCOUNT_CHANGE_PASSWORD_SUCCEED = "Пароль успішно змінено"
    const val SETTINGS_ACCOUNT_LOG_OUT = "Ви дійсно хочете розлогінитися?"
    const val SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT = "При виході з неавторизованого облікового запису ви втратите всі створені дані. Для збереження зареєструйтеся. Все одно вийти?"
    const val SETTINGS_ACCOUNT_EMAIL_DELETE = "Обліковий запис і всі створені дані будуть видалені безповоротно. Щоб продовжити, введіть поточний пароль."
    const val SETTINGS_ACCOUNT_GOOGLE_DELETE = "Обліковий запис і всі створені дані будуть видалені безповоротно. Щоб продовжити, Google може запросити підтвердження авторизації."
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TITLE = "Перенести дані?"
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_ON = "Створені дані будуть перенесені до вашого облікового запису."
    const val SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_OFF = "Створені дані будуть видалені."
    const val SETTINGS_ACCOUNT_EXIST = "Користувач із таким Email вже існує. Ви можете перейти до цього облікового запису. При включеному виборі перенесення даних - вони будуть перенесені в обліковий запис, при вимкненому - видалені безоплатно. Перейти?"
    const val EMPTY_STATE_ACCOUNT = "Ваші дані зберігаються локально в рамках однієї сесії і будуть втрачені при виході з цього облікового запису, очищення кеша або перевстановленні програми. Щоб підключити віддалене зберігання даних та отримати доступ до нього з будь-якого пристрою, зареєструйтесь."
    // Settings language
    const val SETTINGS_LANGUAGE_RUSSIAN = "Російська"
    const val SETTINGS_LANGUAGE_UKRAINIAN = "Українська"
    const val SETTINGS_LANGUAGE_ENGLISH = "Англійська"
    // Settings theme
    const val SETTINGS_THEME_DAY = "Світла"
    const val SETTINGS_THEME_NIGHT = "Темна"
    const val SETTINGS_THEME_AUTO = "Згідно з налаштуваннями телефону"
    // Settings feedback
    const val SETTINGS_FEEDBACK_TITLE = "Якщо у вас є пропозиції або зауваження, ви можете їх описати нижче"
    const val SETTINGS_FEEDBACK_HINT = "Повідомлення"
    const val SETTINGS_FEEDBACK_SEND_BUTTON = "Надіслати"
    const val SETTINGS_FEEDBACK_SEND_SUCCESS = "Повідомлення успішно надіслано!"
    // Settings privacy
    const val PRIVACY_POLICY = "<![CDATA[<html><head><meta charset=\\'utf–8\\'><meta name=\\'viewport\\' content=\\'width=device–width\\'><title>Privacy Policy</title><style>body {font–family: \\'Helvetica Neue\\', Helvetica, Arial, sans–serif;padding: 1em;}</style></head><body><strong>Privacy Policy</strong><p> Tarasov Volodymyr built the SmartBlocker app as a Commercial app. This SERVICE is provided by Tarasov Volodymyr and is intended for use as is. </p><p> This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service. </p><p> If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy. </p><p> The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at SmartBlocker unless otherwise defined in this Privacy Policy. </p><p><strong>Information Collection and Use</strong></p><p> For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way. </p><div><p> The app does use third–party services that may collect information used to identify you. </p><p> Link to the privacy policy of third–party service providers used by the app </p><ul><li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Google Play Services</a></li><li><a href=\"AdMob policies and restrictions – Google AdMob Help target=\" _blank\" rel=\"noopener noreferrer\">AdMob</a></li><li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\" rel=\"noopener noreferrer\">Google Analytics for Firebase</a> </li><li><a href=\"Firebase Support target=\" _blank\" rel=\"noopener noreferrer\">Firebase Crashlytics</a></li></ul></div><p><strong>Log Data</strong></p><p> I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third–party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics. </p><p><strong>Cookies</strong></p><p> Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device\\'s internal memory. </p><p> This Service does not use these “cookies” explicitly. However, the app may use third\\–party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service. </p><p><strong>Service Providers</strong></p><p> I may employ third\\–party companies and individuals due to the following reasons: </p><ul><li>To facilitate our Service;</li><li>To provide the Service on our behalf;</li><li>To perform Service–related services; or</li><li>To assist us in analyzing how our Service is used.</li></ul><p> I want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose. </p><p><strong>Security</strong></p><p> I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security. </p><p><strong>Links to Other Sites</strong></p><p> This Service may contain links to other sites. If you click on a third–party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third–party sites or services. </p><p><strong>Children\\’s Privacy</strong></p><div><p> These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do the necessary actions. </p></div><p><strong>Changes to This Privacy Policy</strong></p><p> I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page. </p><p>This policy is effective as of 2023–01–04</p><p><strong>Contact Us</strong></p><p> If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at blockersmart2022@gmail.com. </p></body></html>]]>"
    // Menu item
    const val MENU_INFO = "Інфо"
    const val MENU_DELETE = "Видалити"
    const val MENU_SEARCH = "Пошук"
    const val ERROR_MESSAGE = "Сталася помилка"
}

fun getStringResourcesByLocale(locale: String): Any {
    return when (locale) {
        APP_LANG_UK -> StringResourcesUK
        APP_LANG_RU -> StringResourcesRU
        else -> StringResourcesEN
    }
}