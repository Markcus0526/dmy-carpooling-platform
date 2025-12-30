//
//  Cus_EvalDriverViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol EvalDrvDelegate;

@interface Cus_EvalDriverViewController : UIViewController<UITextFieldDelegate, UITextViewDelegate>
{
    IBOutlet UIButton *             _radioEvalHigh;
    IBOutlet UIButton *             _radioEvalMedium;
    IBOutlet UIButton *             _radioEvalLow;

    IBOutlet UITextView *           _txtMessage;
    
    IBOutlet UIView *               _vwUsualMsg;
    IBOutlet UIView *               _vwBottom;
    
    IBOutlet UIButton *             _markArrow;
    
    /********************** Member Variable ***********************/
    id                              mParent;
    
    enum EVAL_STATE                 mLevel;
	BOOL							isCommWordsExpand;
}

@property(strong, nonatomic) id<EvalDrvDelegate> delegate;

- (void) setParent : (id)parent;

- (IBAction)onClickedBackground:(id)sender;

- (IBAction)onSelectEvalHigh:(id)sender;
- (IBAction)onSelectEvalMedium:(id)sender;
- (IBAction)onSelectEvalLow:(id)sender;

- (IBAction)onClickedComment1:(id)sender;
- (IBAction)onClickedComment2:(id)sender;
- (IBAction)onClickedComment3:(id)sender;
- (IBAction)onClickedComment4:(id)sender;

- (IBAction)onClickedCancel:(id)sender;
- (IBAction)onClickedSubmit:(id)sender;


- (IBAction)markArrowClick:(id)sender;

@end




// service protocol
@protocol EvalDrvDelegate <NSObject>

@optional

- (void) submitComment : (NSString *)message level:(int)level;

@end