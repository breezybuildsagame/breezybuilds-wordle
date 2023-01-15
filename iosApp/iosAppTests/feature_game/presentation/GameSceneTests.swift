//
//  GameSceneTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/7/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
@testable import iosApp

final class GameSceneTests: XCTestCase
{
    func test_when_view_appears__first_element_is_a_ZStack()
    {
    
    }
    
    func test_when_view_appears__handler_setUp_is_invoked()
    {
        // given
        let sut = GameScene()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().zStack().vStack(0).view(GameSceneHeader.self, 0))
        XCTAssertNoThrow(try sut.inspect().zStack().vStack(0).spacer(1))
        XCTAssertNoThrow(try sut.inspect().zStack().vStack(0).view(GameSceneBoard.self, 2))
        XCTAssertNoThrow(try sut.inspect().zStack().vStack(0).spacer(3))
    }
}
