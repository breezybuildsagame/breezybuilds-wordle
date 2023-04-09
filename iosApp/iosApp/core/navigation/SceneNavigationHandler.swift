//
//  SceneNavigationHandler.swift
//  iosApp
//
//  Created by Bradley Phillips on 4/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import SwiftUIRouter
import shared

class SceneNavigationHandler: SceneNavigationHandleable
{
    @EnvironmentObject var iOSNavigator: Navigator
    
    static let shared: SceneNavigationHandler = SceneNavigationHandler()
    
    var appNavigator: AppNavigationHandleable!
    
    
    init(appNavigator: AppNavigationHandleable? = nil)
    {
        self.appNavigator = appNavigator ?? NavHelper().appNavigator()
    }
    
    func setUp() { appNavigator.setSceneNavigator(newSceneNavigator: self) }
    
    func navigate(route: AppRoute, direction: NavigationDirection)
    {
        print("Navigating to \(route)")
        iOSNavigator.navigate(route.name)
    }
}
