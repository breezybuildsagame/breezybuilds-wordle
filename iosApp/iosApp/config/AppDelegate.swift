//
//  AppDelegate.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import UIKit
import shared

class AppDelegate: NSObject, UIApplicationDelegate
{
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool
    {
        KoinPlatformManager.shared.start(scenarios: [Scenario.wordFound, Scenario.answerSaved])
        
        return true
    }
}
