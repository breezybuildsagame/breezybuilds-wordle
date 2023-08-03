//
//  HelpSheetContentTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 8/3/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp

final class HelpSheetContentTests: XCTestCase
{
    func test_when_view_initialized_with_title_String_paramter__expected_title_is_displayed() throws
    {
        // given
        let expectedTitle = "TEST HOW TO PLAY"
        let sut = HelpSheetContent(testTitle: expectedTitle)
        let expectedBottomPadding = mockFrame().height * (20.0 / idealFrame().height)
        let expectedFontSize = mockFrame().height * (20.0 / idealFrame().height)
        let expectedFrameWidth = mockFrame().width * (300.0 / idealFrame().width)
        let expectedFrameHeight = mockFrame().height * (50.0 / idealFrame().height)
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        let textView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).text(0)
        
        // then
        XCTAssertEqual(expectedTitle, try textView.string())
        XCTAssertEqual("Roboto-Black", try textView.attributes().font().name())
        XCTAssertEqual(expectedFontSize, try textView.attributes().font().size())
        XCTAssertEqual(.center, try textView.multilineTextAlignment())
        XCTAssertEqual(.ui.onSurface, try textView.attributes().foregroundColor())
        XCTAssertEqual(expectedFrameWidth, try textView.fixedWidth())
        XCTAssertEqual(expectedFrameHeight, try textView.fixedHeight())
        XCTAssertEqual(expectedBottomPadding, try textView.padding(.bottom))
    }
    
    func test_when_view_initialized_with_Instruction_list__expected_instructions_are_displayed() throws
    {
        // given
        let expectedDividerHeight = mockFrame().height * (1.0 / idealFrame().height)
        let expectedDividerBottomPadding = mockFrame().height * (20.0 / idealFrame().height)
        let expectedInstructions = [
            HelpSheetContent.Instruction(instruction: "Guess the WORDLE in 6 tries."),
            HelpSheetContent.Instruction(instruction: "Each guess must be a valid 5 letter word. Hit the enter button to submit.")
        ]
        let sut = HelpSheetContent(
            testInstructions: expectedInstructions
        )
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
    
        // then
        try expectedInstructions.enumerated().forEach()
        { (index, instruction) in
            let instructionView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).forEach(1).view(HelpSheetContent.Instruction.self, index)
            
            XCTAssertEqual(expectedInstructions[index], try instructionView.actualView())
        }
        let borderView = try sut.inspect().view(HelpSheetContent.self).zStack().vStack(0).color(2)
        XCTAssertEqual(.ui.error, try borderView.value())
        XCTAssertEqual(expectedDividerHeight, try borderView.fixedHeight())
        XCTAssertEqual(expectedDividerBottomPadding, try borderView.padding(.bottom))
    }
}

extension HelpSheetContent
{
    init(
        testTitle: String = "",
        testInstructions: [HelpSheetContent.Instruction] = []
    )
    {
        self.init(
            title: testTitle,
            instructions: testInstructions
        )
    }
}
