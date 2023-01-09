//
//  SceneDimensionsTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp

final class SceneDimensionsTests: XCTestCase
{
    func test_when_initialized__width_and_height_properties_are_set_to_zero()
    {
        // given
        let expectedDimension: CGFloat = 0
        
        // when
        let sut = SceneDimensions()
        
        // then
        XCTAssertEqual(expectedDimension, sut.width)
        XCTAssertEqual(expectedDimension, sut.height)
    }
    
    func test_when_setDimensions_is_invoked__dimensionsHaveReset_publishes()
    {
        // given
        let expectation = XCTestExpectation(description: "Waiting to set dimensions")
        let sut = SceneDimensions()
        sut.setDimensions(width: 100, height: 100)
        
        let mockView = MockView(expectation: expectation).environmentObject(sut)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView)
        sut.setDimensions(width: mockFrame().width, height: mockFrame().height)
    
        // then
        wait(for: [expectation], timeout: 2)
        XCTAssertEqual(mockFrame().width, sut.width)
        XCTAssertEqual(mockFrame().height, sut.height)
    }
    
    func test_when_setDimensions_provides_dimensions_matching_current__dimensionsHaveReset_does_not_publish()
    {
        // given
        let sut = SceneDimensions()
        
        // when
        sut.setDimensions(width: 0, height: 0)
        
        // then
        XCTAssertFalse(sut.dimensionsHaveReset)
        XCTAssertEqual(0, sut.width)
        XCTAssertEqual(0, sut.height)
    }
    
    struct MockView: View
    {
        @EnvironmentObject var sut: SceneDimensions
        @State var timesSutHasPublished: Int = 0
        
        var maxTimesSutShouldPublish: Int = 1
        var failIfPublishedOverMax: Bool = false
        
        var expectation: XCTestExpectation
        
        var body: some View
        {
            ZStack {}
                .onReceive(sut.objectWillChange)
                { change in
                    timesSutHasPublished += 1
                    
                    if (timesSutHasPublished > maxTimesSutShouldPublish && failIfPublishedOverMax) {
                        XCTFail()
                    }
                    else if (timesSutHasPublished == maxTimesSutShouldPublish) {
                        expectation.fulfill()
                    }
                }
        }
    }
}
