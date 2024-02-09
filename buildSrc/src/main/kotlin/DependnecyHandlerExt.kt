import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.implementation(dependency: String) {
    add("implementation",dependency)
}

fun DependencyHandler.kapt(dependency: String) {
    add("kapt",dependency)
}

fun DependencyHandler.moduleImplementation(dependency: String) {
    add("implementation",project(dependency))
}

fun DependencyHandler.composeFoundationAndroid() {
    implementation(Dependencies.composeFoundationAndroid)
}

fun DependencyHandler.retrofitModule() {
    moduleImplementation(":retrofit_module")
}

fun DependencyHandler.hilt() {
    implementation(Dependencies.hiltWork)
    kapt(Dependencies.hiltCompiler)
    implementation(Dependencies.hiltAndroid)
    implementation(Dependencies.hiltNavigation)
    kapt(Dependencies.hiltAndroidCompiler)
}

fun DependencyHandler.workManager() {
    implementation(Dependencies.work)
}

fun DependencyHandler.accompanist() {
    implementation(Dependencies.accompanistPager)
    implementation(Dependencies.accompanistPagerIndicator)
}

fun DependencyHandler.compose() {
    implementation(Dependencies.composeNavigation)
    implementation(Dependencies.runtimeCompose)
    implementation(Dependencies.composeSplashScreen)
    implementation(Dependencies.composeConstraintLayout)
}

fun DependencyHandler.swipeBox() {
    implementation(Dependencies.swipeBox)
}

fun DependencyHandler.extendedMaterialIcons() {
    implementation(Dependencies.extendedMaterialIcons)
}

fun DependencyHandler.cryptoSecurity() {
    implementation(Dependencies.cryptoSecurity)
}

fun DependencyHandler.kotlinSerialization() {
    implementation(Dependencies.kotlinSerialization)
}

fun DependencyHandler.dataStore() {
    implementation(Dependencies.dateStore)
    implementation(Dependencies.dateStoreCore)
}

fun DependencyHandler.httpLoggingInterceptor() {
    implementation(Dependencies.httpLoggingInterceptor)
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGsonConverter)
}