//
//  AppNavigationHandlerCommonMock.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class AppNavigationHandlerCommonMock: AppNavigationHandleable
{
    var currentRouteToReturn: AppRoute? = nil
    var navigateMethodPassedInRoute: AppRoute? = nil
    var navigateMethodPassedInDirection: NavigationDirection? = nil
    var popBackMethodPassedInNumberOfScreens: Int32? = nil
    var sceneNavigator: SceneNavigationHandleable? = nil
    
    func currentRoute() -> AppRoute? { currentRouteToReturn }
    
    func navigate(route: AppRoute, direction: NavigationDirection)
    {
        navigateMethodPassedInRoute = route
        navigateMethodPassedInDirection = direction
    }
    
    func popBack(numberOfScreens: Int32) { popBackMethodPassedInNumberOfScreens = numberOfScreens }
    
    func setSceneNavigator(newSceneNavigator: SceneNavigationHandleable)
    {
        self.sceneNavigator = newSceneNavigator
    }
}
