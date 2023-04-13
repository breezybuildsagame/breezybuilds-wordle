//
//  AppModalCommonMock.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class AppModalCommonMock: AppModalRepresentable
{
    var contentToReturn: AppModalContentRepresentable? = nil
    var viewHandler: AppModalViewHandleable? = nil
    var setContentMethodPassedInNewContent: AppModalContentRepresentable? = nil
    var setHandlerMethodPassedInNewHandler: AppModalViewHandleable?  = nil
    
    func content() -> AppModalContentRepresentable? { contentToReturn }
    
    func handler() -> AppModalViewHandleable? { viewHandler }
    
    func setContent(newContent: AppModalContentRepresentable?)
    {
        self.setContentMethodPassedInNewContent = newContent
    }
    
    func setHandler(newHandler: AppModalViewHandleable?)
    {
        viewHandler = newHandler
        self.setHandlerMethodPassedInNewHandler = newHandler
    }
}
