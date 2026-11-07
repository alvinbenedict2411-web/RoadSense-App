package com.example.beastmode_5.App.Presentation.Navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class BeastMode5Routes {

    @Serializable
    object LoginScreen:BeastMode5Routes()
    @Serializable
    object SignupScreen:BeastMode5Routes()
    @Serializable
    object HomeScreen: BeastMode5Routes()
    @Serializable
    object LiveFeedScreen: BeastMode5Routes()
    @Serializable
    object StaticFeedScreen: BeastMode5Routes()
    @Serializable
    object DetailsScreen: BeastMode5Routes()
    @Serializable
    data class GoogleMapsScreen(val lat: Double, val long: Double): BeastMode5Routes()
    @Serializable
    object GoogleMapsScreenNoData: BeastMode5Routes()
    @Serializable
    object CloudDetailsScreen: BeastMode5Routes()
    @Serializable
    object UserDetailsScreen: BeastMode5Routes()


}