-injars 'C:\Users\Administrator\Desktop\proguard\input\core_client_1_0_1.jar'
-outjars 'C:\Users\Administrator\Desktop\proguard\out\core_client_out.jar'

-libraryjars 'C:\Program Files\Java\jre7\lib\rt.jar'
-libraryjars 'C:\Users\Administrator\Desktop\proguard\lib\android-support-v4.jar'
-libraryjars 'C:\Users\Administrator\Desktop\proguard\lib\android.jar'
-libraryjars 'C:\Users\Administrator\Desktop\proguard\lib\asmack-didiclient-1-0-1.jar'

-target 1.6
-dontoptimize
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontusemixedcaseclassnames
-keeppackagenames [com.ihengtu.xmpp.core.login],[com.ihengtu.xmpp.core.helper],[com.ihengtu.xmpp.core.manager]
-keepattributes InnerClasses,LineNumberTable
-dontpreverify
-verbose
-dontwarn


-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.View

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keep class android.net.http.SslError

-keep class android.webkit.** {
    <fields>;
    <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class **.R$* {
    <fields>;
    <methods>;
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class com.ihengtu.xmpp.core.login.** {
    <fields>;
    <methods>;
}

-keep class com.ihengtu.xmpp.core.helper.** {
    <fields>;
    <methods>;
}

-keep class com.ihengtu.xmpp.core.handler.** {
    <fields>;
    <methods>;
}

-keep class com.ihengtu.xmpp.core.manager.LoginManager {
    <fields>;
    <methods>;
}

-keep class com.ihengtu.xmpp.core.manager.PresenceManager {
    <fields>;
    <methods>;
}

-keep class com.ihengtu.xmpp.core.manager.PresenceManager$PresenceChangedLiestener {
    <fields>;
    <methods>;
}

-keep class com.ihengtu.xmpp.core.manager.ConnectionManager {
    public static <fields>;
    public <methods>;
    public static <methods>;
    public static synthetic <methods>;
}

-keep class com.ihengtu.xmpp.core.XmppAction {
    <fields>;
}

-keep class com.ihengtu.xmpp.core.service.ImService {
    public <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# Remove debugging - All logging API calls. Remove all invocations of the
# logging API whose return values are not used.
-assumenosideeffects public class java.util.logging.* {
    <methods>;
}

# Remove debugging - All Log4j API calls. Remove all invocations of the
# Log4j API whose return values are not used.
-assumenosideeffects public class org.apache.log4j.** {
    <methods>;
}
