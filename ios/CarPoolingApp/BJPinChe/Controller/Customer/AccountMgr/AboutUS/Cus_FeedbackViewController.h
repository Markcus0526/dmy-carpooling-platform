//
//  FeedbackViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CPTextViewPlaceholder.h"

@interface Cus_FeedbackViewController : SuperViewController <UITextFieldDelegate, UITextViewDelegate, TradeSvcDelegate>
{
    /********************** UI Controls ***************************/
    IBOutlet CPTextViewPlaceholder *    _txtComment;
}

- (IBAction)onClickedBack:(id)sender;

@end
