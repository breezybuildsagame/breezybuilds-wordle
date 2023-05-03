//
//  AppSheetViewHandler.swift
//  iosApp
//
//  Created by Bradley Phillips on 5/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class AppSheetViewHandler: AppSheetViewHandleable
{
    var appSheet: AppSheetRepresentable!
    
    init(appSheet: AppSheetRepresentable? = nil)
    {
        self.appSheet = appSheet ?? AppSheetHelper().appSheet()
    }
    
    func setUp()
    {
        appSheet.setHandler(newHandler_: self)
    }
    
    func onSheetShouldHide(animationDuration: Int64)
    {
        
    }
    
    func onSheetShouldShow(animationDuration: Int64)
    {
        
    }
}
