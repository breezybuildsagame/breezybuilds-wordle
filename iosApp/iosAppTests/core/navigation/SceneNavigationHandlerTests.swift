//
//  SceneNavigationHandlerTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import XCTest
@testable import iosApp

final class SceneNavigationHandlerTests: XCTestCase
{
    func test_when_setUp_invoked__AppNavigationHandleable_setHandler_method_is_invoked_passing_in_self()
    {
        // given
        let mockNavigator = AppNavigationHandlerCommonMock()
        let sut = SceneNavigationHandler(appNavigator: mockNavigator)
        
        // when
        sut.setUp()
        
        // then
        XCTAssertNotNil(mockNavigator.sceneNavigator)
    }
}
