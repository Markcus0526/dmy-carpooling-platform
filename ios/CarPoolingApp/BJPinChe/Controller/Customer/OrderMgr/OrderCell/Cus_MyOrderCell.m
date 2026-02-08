//
//  Cus_OrderSingleCell.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_MyOrderCell.h"
#import "Cus_OrderMgrViewController.h"
#import "ImageHelper.h"

@implementation Cus_MyOrderCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        _lblTime.font =[UIFont systemFontOfSize:12];
    }
    return self;
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void) initData : (STOrderInfo *)data parent:(id)parent
{
    mOrderInfo = data;
    mParent = parent;
    if (mOrderInfo.type == TYPE_LONG_ORDER) {
        [_lblStartPos setText:mOrderInfo.start_city];
        [_lblEndPos setText:mOrderInfo.end_city];
    }else
    {
        [_lblStartPos setText:mOrderInfo.startPos];
        [_lblEndPos setText:mOrderInfo.endPos];
    }
    
    
    // set sex image & change age color according to sex
    //_imgSex
    //if ([mOrderInfo.sex isEqualToString:SEX_MALE]) {
    
    if ([mOrderInfo.image length] > 0) {
        [_imgUser setImageWithURL:[NSURL URLWithUnicodeString:mOrderInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    }
    
    // set sex image & change age color according to sex
    if(mOrderInfo.sex == SEX_MALE) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = [UIColor orangeColor];
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mOrderInfo.age]];
    [_lblName setText:mOrderInfo.name];
    
    // change button image by order state
    
    // check order state & change operate button image
    [_btnOperate setBackgroundImage:nil forState:UIControlStateNormal];
    [_btnOperate setTitle:@"" forState:UIControlStateNormal];
    [_btnOperate setHidden:NO];
    switch (mOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        {
			[_btnOperate setHidden:YES];
			break;
        }
        case ORDER_STATE_PUBLISHED:
        {
            [_btnOperate setHidden:YES];
            break;
        }
        case ORDER_STATE_GRABBED:
        {
            [_btnOperate setHidden:YES];
//            [_btnOperate setTitle:@"待执行" forState:UIControlStateNormal];
//            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
//            _btnOperate.userInteractionEnabled =NO;
//			_btnOperate.hidden = NO;
            break;
        }
        case ORDER_STATE_STARTED:
		{
			[_btnOperate setTitle:@"开始执行" forState:UIControlStateNormal];
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled = NO;
			_btnOperate.hidden = NO;
            break;
        }
        case ORDER_STATE_DRIVER_ARRIVED:
        {
            [_btnOperate setTitle:@"车主到达" forState:UIControlStateNormal];
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled = NO;
            _btnOperate.hidden = NO;
            break;

        }
        case ORDER_STATE_PASSENGER_GETON:
        {
            [_btnOperate setTitle:@"乘客上车" forState:UIControlStateNormal];
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled = NO;
            _btnOperate.hidden = YES;
            break;
        }
        case ORDER_STATE_FINISHED:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_pay.png"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled = YES;
//			_btnOperate.hidden = YES;
            break;
        }
        case ORDER_STATE_PAYED:
        {
			[_btnOperate setTitle:@"去评价" forState:UIControlStateNormal];
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_evalution.png"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled = YES;
			_btnOperate.hidden = NO;
			break;
        }
        case ORDER_STATE_EVALUATED:
        {
            // set evaluate image by state
            switch (mOrderInfo.evaluated) {
                case EVAL_NOT:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_evalution.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_HIGH:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_MEDIUM:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_LOW:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
                    break;
                default:
                    break;
            }
			_btnOperate.hidden = NO;
            break;
        }
        case ORDER_STATE_CLOSED:
        case  ORDER_STATE_Ticket_REFUND:
        {
            //[_btnOperate setTitle:@"" forState:UIControlStateNormal];
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_exit"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled =NO;
			_btnOperate.hidden = NO;
            break;
        }
        default:
            break;
    }

	[_lblName setText:mOrderInfo.name];
    [_lblPrice setText:[NSString stringWithFormat:@"%.0f%@", mOrderInfo.price, DIAN]];
    [_lblType setText:mOrderInfo.type_desc];
    [_lblTime setText:mOrderInfo.start_time];

	[_lblState setText:mOrderInfo.state_desc];
}


- (IBAction)onClickedPerform:(id)sender
{
	TEST_NETWORK_RETURN;
//	[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
	
    Cus_OrderMgrViewController * ctrl = (Cus_OrderMgrViewController *)mParent;
    [ctrl onClickedPerform:mOrderInfo];
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

#pragma OrderExecuteSvcMgr Delegate Methods
- (void) duplicateUser:(NSString *)result
{
    NSLog(@"Detect duplicate user");
    
    [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
//	[mParent duplicateLogout];
}


- (void)evaluateOnceOrderDriverResult:(NSString *)result Level:(int)level
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		mOrderInfo.state = ORDER_STATE_EVALUATED;
		switch (level) {
			case EVAL_NOT:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
				break;
			case EVAL_HIGH:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
				break;
			case EVAL_MEDIUM:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
				break;
			case EVAL_LOW:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
				break;
			default:
				break;
		}
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void)evaluateOnOffOrderDriverResult:(NSString *)result Level:(int)level
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		mOrderInfo.state = ORDER_STATE_EVALUATED;
		switch (level) {
			case EVAL_NOT:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
				break;
			case EVAL_HIGH:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
				break;
			case EVAL_MEDIUM:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
				break;
			case EVAL_LOW:
				[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
				break;
			default:
				break;
		}
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}




@end
