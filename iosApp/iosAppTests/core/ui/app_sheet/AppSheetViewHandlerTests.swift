//
//  AppSheetViewHandlerTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 5/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp

final class AppSheetViewHandlerTests: XCTestCase
{
    func test_when_setUp_invoked__AppSheet_setHandler_method_is_invoked_passing_in_self()
    {
        // given
        let appSheetMock = AppSheetCommonMock()
        let sut = AppSheetViewHandler(appSheet: appSheetMock)
        appSheetMock.setHandlerPassedInNewHandler = nil
        
        // when
        sut.setUp()
        
        // then
        XCTAssertNotNil(appSheetMock.setHandlerPassedInNewHandler)
    }
    
    func test_when_onSheetShouldShow_invoked__expected_view_is_displayed() throws
    {
        // given
        
        // when
        
        // then
    }
    
    func test_when_onSheetShouldHide_invoked__expected_view_is_displayed() throws
    {
        // given
        
        // when
        
        // then
    }
}
