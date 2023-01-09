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

extension GameScene: Inspectable { }

final class GameSceneTests: XCTestCase
{
    func testViewInspectorWorks()
    {
        // given
        let sut = GameScene()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut)
        
        // then
        XCTAssertNoThrow(try sut.inspect().zStack()[0].text())
    }
}
