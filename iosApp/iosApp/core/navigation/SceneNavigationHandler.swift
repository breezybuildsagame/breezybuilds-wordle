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
    var iOSNavigator: Navigator?
    
    static let shared: SceneNavigationHandler = SceneNavigationHandler()
    
    var appNavigator: AppNavigationHandleable!
    
    
    init(appNavigator: AppNavigationHandleable? = nil)
    {
        self.appNavigator = appNavigator ?? NavHelper().appNavigator()
    }
    
    func setUp(navigator: Navigator? = nil)
    {
        self.iOSNavigator = navigator
        appNavigator.setSceneNavigator(newSceneNavigator: self)
    }
    
    func navigate(route: AppRoute, direction: NavigationDirection)
    {
        print("Navigating to \(route)")
        Task { await MainActor.run {iOSNavigator?.navigate(route.name) } }
    }
}
